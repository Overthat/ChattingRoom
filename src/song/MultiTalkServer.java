package song;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiTalkServer {
    static int clientnum = 0;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(4700);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4700.");
            System.exit(-1);
        }
        System.out.println("Server is running!");
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        while (listening) {
            try {
                Socket s = serverSocket.accept();
                // ServerThread st = new ServerThread(serverSocket.accept(), clientnum);
                threadPool.submit(new ServerReceiveThread(s));
                threadPool.submit(new ServerSendThread(s));
                System.out.printf("client-%d ready!%n", clientnum+1);
            } catch (IOException e) {}
            clientnum++;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO: handle exception
        }
    }
}

// class ServerThread implements Runnable {

//     Socket socket = null;
//     int clientnum;

//     public ServerThread(Socket socket, int num) {
//         this.socket = socket;
//         clientnum = num + 1;
//     }

//     @Override
//     public void run() {
//         //
//     }
// }

class ServerReceiveThread implements Runnable {
 
    private Socket s;
 
    public ServerReceiveThread(Socket s) {
        this.s = s;
    }
 
    @Override
    public void run() {
        try {
        	// Thread.currentThread().setName("");
            DataInputStream dis = new DataInputStream(s.getInputStream());

            while (true) {
                String msg = dis.readUTF();
                System.out.println("recv: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
 
}

class ServerSendThread implements Runnable {

    private Socket s;

    public ServerSendThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            while (true) {
                Scanner sc = new Scanner(System.in);
                String str = sc.nextLine();
                dos.writeUTF(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
