package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements IClient, AutoCloseable, Runnable {

    private static final String DOWNLOAD_DOCUMENT = "DOWNLOAD_DOCUMENT";
    private static final String UPLOAD_DOCUMENT = "UPLOAD_DOCUMENT";
    private static final String LIST_DOCUMENTS = "LIST_DOCUMENTS";

    private static final String END_OF_LIST = "END_OF_LIST";

    private static final int PORT = 5000;
    private static final String HOST = "localhost";
    private Socket socket;
    private BufferedReader userInput;
    private PrintWriter userOutput;
    private BufferedReader fromServer;
    private PrintWriter toServer;

    public Client(InputStream userInput, OutputStream userOutput) throws IOException {
        socket = new Socket(HOST, PORT);
        this.userInput = new BufferedReader(new InputStreamReader(userInput));
        this.userOutput = new PrintWriter(userOutput, true);
        this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.toServer = new PrintWriter(socket.getOutputStream(), true);
    }

    private void printMenu() throws IOException {
        userOutput.println("| 0 - download document");
        userOutput.println("| 1 - list documents");
        userOutput.println("| 2 - upload content");
    }

    public void handleUploadDocument() throws IOException {
        toServer.println(UPLOAD_DOCUMENT);
    };

    public void handleDownloadDocument() throws IOException {
        toServer.println(DOWNLOAD_DOCUMENT);
    };

    public void handleListDocuments() throws IOException {
        toServer.println(LIST_DOCUMENTS);

        String msg = null;

        while ((msg = fromServer.readLine()) != null) {
            if (msg.equals(END_OF_LIST)) {
                break;
            }
            userOutput.println(msg);
        }
    };

    public void handleWarning(String msg) throws IOException {
        StringBuilder sb = new StringBuilder("| Warning: ");
        sb.append(msg);
        String errorMsg = sb.toString();
        userOutput.println(errorMsg);
    }

    private Integer getMenuOption() throws NumberFormatException {
        try {
            String optionString = userInput.readLine();
            Integer option = Integer.parseInt(optionString);
            return option;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void handleMenuOption(Integer option) {
        try {
            switch (option) {
                case 0:
                    handleUploadDocument();
                    break;
                case 1:
                    handleListDocuments();
                    break;
                case 2:
                    handleListDocuments();
                    break;
                default:
                    handleWarning("Invalid option");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                printMenu();
                Integer option = getMenuOption();
                handleMenuOption(option);
            } catch (NumberFormatException ex) {
                handleMenuOption(4);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void close() throws Exception {
        socket.close();
        fromServer.close();
        toServer.close();
    }

    public static void main(String[] args) {
        try {
            Thread client = new Thread(new Client(System.in, System.out));
            client.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}