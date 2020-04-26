package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.io.InputStreamReader;

public class ClientHandler implements IClientHandler, Runnable, AutoCloseable {

    private static final String DOWNLOAD_DOCUMENT = "DOWNLOAD_DOCUMENT";
    private static final String UPLOAD_DOCUMENT = "UPLOAD_DOCUMENT";
    private static final String LIST_DOCUMENTS = "LIST_DOCUMENTS";

    private static final String END_OF_DOCUMENT = "END_OF_DOCUMENT";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String END_OF_LIST = "END_OF_LIST";

    private Socket client;
    private LinkedHashSet<String> index;
    private BufferedReader fromClinet;
    private PrintWriter toClient;

    public ClientHandler(ServerSocket ss, LinkedHashSet<String> index) throws IOException {
        client = ss.accept();
        this.index = index;
        fromClinet = new BufferedReader(new InputStreamReader(client.getInputStream()));
        toClient = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void handleDownloadDocument(BufferedReader fromClient, PrintWriter toClient) throws IOException {
        System.out.println("handleDownloadDocument");
    };

    @Override
    public void handleUploadDocument(BufferedReader fromClient, PrintWriter toClient) throws IOException {
        System.out.println("handleUploadDocument");

    };

    @Override
    public void handleListDocuments(PrintWriter toClient) throws IOException {
        System.out.println("handleListDocuments");
        for (String fileName : index) {
            toClient.println(fileName);
        }
        toClient.println(END_OF_LIST);

    };

    @Override
    public void handleUnknownRequest(PrintWriter toClient) throws IOException {
        System.out.println("handleUnknownRequest");
        try {
            close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {

            try {
                String action = fromClinet.readLine();

                System.out.println(action);

                switch (action) {
                    case DOWNLOAD_DOCUMENT:
                        handleDownloadDocument(fromClinet, toClient);
                        break;
                    case LIST_DOCUMENTS:
                        handleListDocuments(toClient);
                        break;
                    case UPLOAD_DOCUMENT:
                        handleUploadDocument(fromClinet, toClient);
                        break;
                    default:
                        handleUnknownRequest(toClient);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (client != null) {
            client.close();
        }
    };
}