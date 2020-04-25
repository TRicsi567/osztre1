package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements IClientHandler, Runnable {

    private Socket clinet;

    public ClientHandler(Socket client) throws IOException {
        System.out.println(client.getInetAddress().getHostAddress() + " csatlakozott");
        this.clinet = client;
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String msg = input.readLine();
        System.out.println(msg);
    }

    @Override
    public void handleDownloadDocument(BufferedReader fromClient, PrintWriter toClient) throws IOException {
    };

    @Override
    public void handleUploadDocument(BufferedReader fromClient, PrintWriter toClient) throws IOException {
    };

    @Override
    public void handleListDocuments(PrintWriter toClient) throws IOException {
    };

    @Override
    public void handleUnknownRequest(PrintWriter toClient) throws IOException {
    }

    @Override
    public void run() {

    };
}