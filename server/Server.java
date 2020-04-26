package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<Thread> clients;

    public Server(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        System.out.println("Server socket is listening at: " + port.toString());
    }

    public void start() throws IOException {
        for (int i = 0; i < 2; ++i) {
            Socket client = serverSocket.accept();
            Thread clienThread = new Thread(new ClientHandler(client));
            clients.add(clienThread);
            clienThread.start();
        }
        serverSocket.close();
    }

    public static void main(String[] args) {
        try {
            (new Server(4000)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}