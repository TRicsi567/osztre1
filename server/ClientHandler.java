package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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

        String fileName = fromClient.readLine();

        synchronized(fileName.intern()) {

            if(!index.contains(fileName)) {
                toClient.println(NOT_FOUND);
                toClient.println(END_OF_DOCUMENT);
                return;
            }
    
            try (BufferedReader file = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
                String line = null;
        
                while((line = file.readLine()) != null) {
                    toClient.println(line);
                }
                toClient.println(END_OF_DOCUMENT);
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    };

    @Override
    public void handleUploadDocument(BufferedReader fromClient, PrintWriter toClient) throws IOException {
        String fileName = fromClient.readLine();

        synchronized(this) {
            index.add(fileName);
        }

        synchronized(fileName.intern()) {
            try(BufferedWriter file = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8 )) ) {
                
                String line = null;
                while((line = fromClient.readLine())!=null) {
                    if(line.equals(END_OF_DOCUMENT)) {
                        break;
                    }
                    file.write(line, 0, line.length());
                    file.write(System.lineSeparator());
                }
                file.flush();
                file.close();
    
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    };

    @Override
    public void handleListDocuments(PrintWriter toClient) throws IOException {
        for (String fileName : index) {
            toClient.println(fileName);
        }
        toClient.println(END_OF_LIST);

    };

    @Override
    public void handleUnknownRequest(PrintWriter toClient) throws IOException {
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
        fromClinet.close();
        toClient.close();
    };
}