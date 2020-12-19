package song;

import java.net.*;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;

public class TalkClient {
    private String ip;
    private int port;
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
                System.out.println("From Sever: " + is.readLine());
                System.out.print("send: ");
                readline = sin.readLine();
            }
            os.close();
            is.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    public TalkClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public TalkClient() {
        this.ip = "127.0.0.1";
        this.port = 4700;
    }
}