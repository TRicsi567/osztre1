package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class Server {

    private ServerSocket serverSocket;
    private LinkedHashSet<String> index;
    private static final Integer PORT = 5000;

    public Server(String[] initIndex) throws IOException {
        serverSocket = new ServerSocket(PORT);
        index = new LinkedHashSet<>(Arrays.asList(initIndex));

        try {
            while(true) {
                Thread clienThread = new Thread(new ClientHandler(serverSocket, index));
                clienThread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            serverSocket.close();
        }
    }

    public static void main(String[] args) {
        try {
            new Server(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}