package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client implements IClient, AutoCloseable, Runnable {

    private static final int PORT = 5000;
    private static final String HOST = "localhost";
    private Socket socket;
    private BufferedReader userInput;
    private BufferedWriter userOutput;
    private BufferedReader fromServer;
    private BufferedWriter toServer;

    public Client(InputStream userInput, OutputStream userOutput) throws IOException {
        socket = new Socket(HOST, PORT);
        this.userInput = new BufferedReader(new InputStreamReader(userInput));
        this.userOutput = new BufferedWriter(new OutputStreamWriter(userOutput));
        this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private static void printMenu() {
        System.out.println("| 0 - download document");
        System.out.println("| 1 - list documents");
        System.out.println("| 2 - upload content");
    }

    public void handleUploadDocument() throws IOException {
    };

    public void handleDownloadDocument() throws IOException {
    };

    public void handleListDocuments() throws IOException {
    };

    public void handleWarning (String msg) throws IOException{
        StringBuilder sb = new StringBuilder("| Warning: ");
        sb.append(msg);
        String errorMsg = sb.toString();
        userOutput.write(errorMsg, 0, errorMsg.length());
        userOutput.newLine();
        userOutput.flush();
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
            switch(option) {
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

        int hax = 0;

        while(true) {
            if(hax % 2 == 0) {
                printMenu();
            } else {
                try {
                    Integer option = getMenuOption();
                    handleMenuOption(option);
                } catch (NumberFormatException ex) {
                    handleMenuOption(4);
                }
            }
            ++hax;
        }

    }
    

	@Override
	public void close() throws Exception {
		if(socket != null) {
            socket.close();
        }
        if(fromServer != null) {
            fromServer.close();
        }
        if(toServer != null) {
            toServer.close();
        }
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