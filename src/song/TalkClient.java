package song;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端通信线程类. 每个TalkClient里有两个分别负责 接受消息和读取消息的子线程 以实现全双工通信
 */
public class TalkClient extends Thread {
    private String ip;
    private int port;
    // 和GUI通信的对象
    private ClientWin win;

    @Override
    public void run() {
        try {
            Socket s = new Socket(ip, port);
            // 启动发送消息线程
            new ClientSendThread(s, win).start();
            // 启动接受消息线程
            new ClientReceiveThread(s, win).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 关闭方法
    public void close() {
        System.exit(0);
    }

    // 构造方法
    public TalkClient(String ip, int port, ClientWin win) {
        this.ip = ip;
        this.port = port;
        this.win = win;
    }

    // 默认构造方法,连接本机客户端
    public TalkClient() {
        this.ip = "127.0.0.1";
        this.port = 4700;
        win = null;
    }
}

class ClientReceiveThread extends Thread {

    private Socket s;
    private ClientWin win;

    public ClientReceiveThread(Socket s, ClientWin win) {
        this.s = s;
        this.win = win;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            while (true) {
                if (win == null)
                    break;
                String msg = dis.readUTF();
                win.loadComments(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientSendThread extends Thread {

    private Socket s;
    private ClientWin win;

    public ClientSendThread(Socket s, ClientWin win) {
        this.s = s;
        this.win = win;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            while (true) {
                if (win == null)
                    break;
                String str = win.getText();
                dos.writeUTF(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
