package song;

import java.net.*;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;

public class TalkClient extends Thread {
    private String ip;
    private int port;
    private ClientWin win;

    @Override
    public void run() {
        try (Socket socket = new Socket(ip, port)) {
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("client ready!");
            System.out.print("send: ");
            String text = win.getText();
            while (!text.equalsIgnoreCase("bye")) {
                os.println(text);
                os.flush();
                String comments = is.readLine();
                System.out.println("From Sever: " + comments);
                win.loadComments(comments);
                System.out.print("send: ");
                text = win.getText();
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