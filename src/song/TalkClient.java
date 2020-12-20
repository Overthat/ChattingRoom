package song;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TalkClient extends Thread {
    private String ip;
    private int port;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		}

    public TalkClient(String ip, int port, ClientWin win) {
        this.ip = ip;
        this.port = port;
        this.win = win;
    }

    public TalkClient() {
        this.ip = "127.0.0.1";
        this.port = 4700;
        win = null;
    }

    public static void main(String[] args) {
        new TalkClient().start();
    }

}

class ClientReceiveThread extends Thread {
 
    private Socket s;
    private ClientWin win;
 
    public ClientReceiveThread(Socket s, ClientWin win) {
        this.s = s;
        this.win = win;
    }
 
    public void run() {
        try {
        	// Thread.currentThread().setName("");
            DataInputStream dis = new DataInputStream(s.getInputStream());
            while (true) {
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

    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            while (true) {
                String str = win.getText();
                dos.writeUTF(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
