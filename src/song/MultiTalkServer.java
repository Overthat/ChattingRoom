package song;

import java.net.*;

import java.io.*;

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
        while (listening) {
            try {
                new ServerThread(serverSocket.accept(), clientnum).start();
                System.out.printf("client-%d ready!%n", clientnum+1);
            } catch (IOException e) {

            }
            clientnum++;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO: handle exception
        }
    }
}

class ServerThread extends Thread {
    Socket socket = null;
    int clientnum;

    public ServerThread(Socket socket, int num) {
        this.socket = socket;
        clientnum = num + 1;
    }

    public void run() {
        try {
            String line;
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Client-" + clientnum + ": " + is.readLine());
            line = sin.readLine();
            while (!line.equalsIgnoreCase("bye")) {
                os.println(line);
                os.flush();
                System.out.println("Client-" + clientnum + ": " + is.readLine());
                line = sin.readLine();
            }
            System.out.printf("clinet-%d quit.%n", clientnum);
            os.close();
            is.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }
}