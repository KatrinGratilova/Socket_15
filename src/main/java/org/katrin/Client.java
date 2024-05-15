package org.katrin;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private BufferedReader serverReader;
    private PrintWriter writer;
    private Scanner scanner;
    private boolean serverFinished = false;

    public void run(String host, int port) {
        try {
            try (Socket clientSocket = new Socket(host, port)) {
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                scanner = new Scanner(System.in);

                readServer();   // server says hello
                answerServer(); // the client will respond
                readServer();   // if the client greeted in Ukrainian, the server will respond and end the connection

                // check for server status before calling the following methods
                if (!serverFinished) { // if the client did not greet in Ukrainian, communication continues
                    answerServer();
                    readServer();
                }
            } catch (UnknownHostException e) {
                logger.warning(ClientMessages.SERVER_NOT_FOUND_EXCEPTION.getMessage());
            } finally {
                if (writer != null) {
                    serverReader.close();
                    writer.close();
                    scanner.close();
                }
            }
        } catch (IOException e) {
            logger.warning(ClientMessages.IO_EXCEPTION.getMessage());
        }
    }

    void readServer() throws IOException {
        System.out.println(ClientMessages.WAITING_SERVER_RESPONSE.getMessage());
        String serverLine = serverReader.readLine();
        System.out.println(ClientMessages.SERVER_MESSAGE.getMessage() + serverLine);
        if (serverLine.contains("З'єднання буде перервано")) {
            serverFinished = true;
        }
    }

    void answerServer() {
        System.out.print(ClientMessages.WRITE_TO_SERVER.getMessage());
        String clientLine = scanner.nextLine();
        writer.println(clientLine);
    }
}
