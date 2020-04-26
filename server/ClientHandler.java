package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;

public class ClientHandler implements IClientHandler, Runnable, AutoCloseable {

    private static final String DOWNLOAD_DOCUMENT = "DOWNLOAD_DOCUMENT";
    private static final String UPLOAD_DOCUMENT = "UPLOAD_DOCUMENT";
    private static final String LIST_DOCUMENTS = "LIST_DOCUMENTS";

    private static final String END_OF_DOCUMENT = "END_OF_DOCUMENT";
    private static final String NOT_FOUND  = "NOT_FOUND";
    
    private Socket client;
    private LinkedHashSet<String> index;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(ServerSocket ss, LinkedHashSet<String> index) throws IOException {
        client = ss.accept();
        this.index = index;
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
        try {
            String action = input.readLine();

            System.out.println(action);
    
                switch(action) {
                    case DOWNLOAD_DOCUMENT: 
                        handleDownloadDocument(input, output);
                        break;
                    case LIST_DOCUMENTS:
                        handleListDocuments(output);
                        break;
                    case UPLOAD_DOCUMENT: 
                        handleUploadDocument(input, output);
                        break;
                    default:
                        handleUnknownRequest(output);
                }        

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

	@Override
	public void close() throws Exception {
        if(client != null) {
            client.close();
        }
	};
}