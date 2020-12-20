package song;

import java.net.*;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;

public class TalkClient extends Thread{
    private String ip;
    private int port;
    private ClientWin win;
    @Override
    public void run() {
        try (Socket socket = new Socket(ip, port)) {
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("client ready!");
            System.out.print("send: ");
            String readline;
            readline = sin.readLine();
            while (!readline.equalsIgnoreCase("bye")) {
                os.println(readline);
                os.flush();
                String comments = is.readLine();
                System.out.println("From Sever: " + comments);
                win.loadComments(comments);
                System.out.print("send: ");
                readline = sin.readLine();
            }
            os.close();
            is.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
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