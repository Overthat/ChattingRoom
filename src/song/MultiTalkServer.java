package song;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程服务器类.并实现Subject(观察者模式)接口.
 * MultiTalkServer类监听客户端请求启动若干个服务线程ServerThread,并统计数目.
 */
public class MultiTalkServer implements Subject {

    // 观察者模式注册的Observer列表,在此Observer替换为ServerThread
    private List<ServerThread> observerList = new ArrayList<>();
    // ServerThread数目
    private static int clientnum = 0;
    // 监听端口
    private static int port = 4700;

    public static void main(String[] args) {

        var talkserver = new MultiTalkServer();
        ServerSocket serverSocket = null;
        // 监听循环标志
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.printf("Could not listen on port: %d.%n", port);
            System.exit(-1);
        }
        System.out.println("Server is running!");
        // 利用线程池管理各个ServerThread线程
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        // 监听循环
        while (listening) {
            try {
                Socket s = serverSocket.accept();
                // 新建一个ServerThread线程
                var st = new ServerThread(s, talkserver);
                // 注册观察者
                talkserver.attach(st);
                // 提交至线程池
                threadPool.submit(st);
                System.out.printf("client-%d ready!%n", clientnum + 1);
            } catch (IOException e) {
            // TODO: handle exception
            }
            clientnum++;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    // 注册观察者方法
    @Override
    public void attach(ServerThread observer) {
        observerList.add(observer);
    }

    // 注销观察者方法
    @Override
    public void detach(ServerThread observer) {
        observerList.remove(observer);
    }

    // 通知观察者方法
    @Override
    public void notifyChanged(String msg) {
        for (ServerThread observer : observerList) {
            observer.update(msg);
        }
    }
}

/**
 * 服务器线程类,每个都包含收发消息两个线程,以实现全双工通信.
 */
class ServerThread implements Runnable {

    // 父线程对象
    private MultiTalkServer server;
    // 客户端发来的消息
    public volatile String message;
    private Socket socket = null;

    // 构造方法
    public ServerThread(Socket socket, MultiTalkServer server) {
        this.socket = socket;
        this.server = server;
        message = null;
    }

    @Override
    public void run() {
        // 新建两个收发消息线程并启动
        var send = new Thread(new ServerSendThread(socket, this));
        var receive = new Thread(new ServerReceiveThread(socket, this));
        send.start();
        receive.start();
    }

    // 负责阻塞的方法
    public synchronized void myWait() throws InterruptedException {
        wait(); // 不需要放入循环中的wait()方法
    }

    // 子线程接受消息后被调用
    public void msgRecieved(String msg) {
        server.notifyChanged(msg);
    }

    // 得到主线程update信号后,传达消息并通知唤醒
    public synchronized void update(String msg) {
        message = msg;
        notify();
    }
}


/**
 * 接收消息服务器线程.读取客户端发送来的消息后
 * 调用ServerThread的msgRecieved方法
 * 通知主服务器线程回显消息
 */
class ServerReceiveThread implements Runnable {

    private Socket s;
    private ServerThread ser;

    // 构造方法
    public ServerReceiveThread(Socket s, ServerThread ser) {
        this.s = s;
        this.ser = ser;
    }

    @Override
    public void run() {
        try {
            // 装配数据输入流
            DataInputStream dis = new DataInputStream(s.getInputStream());

            // 循环判断消息是否收到
            while (true) {
                // I/O阻塞
                String msg = dis.readUTF();
                if (msg == null || msg.equals(""))
                    continue;
                ser.msgRecieved(msg);
                System.out.println("recv: " + msg);
            }
        } catch (IOException e) {
            // TODO
        }
    }
}

/**
 * 发送消息服务器线程.得到父线程传达的消息后
 * 回显消息
 */
class ServerSendThread implements Runnable {

    private Socket s;
    private ServerThread ser;

    // 构造方法
    public ServerSendThread(Socket s, ServerThread ser) {
        this.s = s;
        this.ser = ser;
    }

    @Override
    public void run() {
        try {
            // 装配数据输出流
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // 循环判断是否发送消息
            while (true) {
                if (ser.message != null) {
                    dos.writeUTF(ser.message);
                    ser.message = null;
                    continue;
                }
                ser.myWait();
            }
        } catch (IOException e) {
            // TODO
        } catch (InterruptedException e) {
            // TODO
        }
    }
}

/**
 * 观察者主题对象
 */
interface Subject {

    /**
     * 订阅操作
     */
    void attach(ServerThread observer);

    /**
     * 取消订阅操作
     */
    void detach(ServerThread observer);

    /**
     * 通知变动
     */
    void notifyChanged(String msg);
}