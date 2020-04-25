package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client implements IClient {

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    private static void printMenu() {
        System.out.println("| 0 - download document");
        System.out.println("| 1 - list documents");
        System.out.println("| 2 - upload content");
    }

    private static void printWarning(String msg) {
        StringBuilder sb = new StringBuilder("| Warning: ");
        sb.append(msg);
        System.out.println(sb.toString());
    }

    public Client(String host, Integer port) throws IOException {
        socket = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(System.in));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void handleUploadDocument() throws IOException {
    };

    public void handleDownloadDocument() throws IOException {
    };

    public void handleListDocuments() throws IOException {
    };

    public void start() {
        try {
            printMenu();
            String _option = input.readLine();
            Integer option = Integer.parseInt(_option);
            System.out.println("A választásod: " + option.toString());
            output.write(_option, 0, _option.length());
            output.write('\n');
            output.flush();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 4000);
            client.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}