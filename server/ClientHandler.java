package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements IClientHandler, Runnable {

    private Socket clinet;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket client) throws IOException {
        System.out.println(client.getInetAddress().getHostAddress() + " csatlakozott");
        this.clinet = client;
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        
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

    }

    @Override
    public void run(){
        Integer action = 4;

        try {
            action = Integer.parseInt(input.readLine());
        } catch (NumberFormatException ex) {
            action = 4;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(action);

        try {
            switch(action) {
                case 0: 
                    handleDownloadDocument(input, output);
                    break;
                case 1:
                    handleListDocuments(output);
                    break;
                case 2: 
                    handleUploadDocument(input, output);
                    break;
                default:
                    handleUnknownRequest(output);
            }        
        } catch(IOException ex) {
            ex.printStackTrace();
        }


    };
}