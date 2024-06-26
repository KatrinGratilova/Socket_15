package org.katrin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private BufferedReader clientReader;
    private PrintWriter writer;
    private Socket clientSocket;

    public void run(int port) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Сервер слухає запити на " + port + ". Очікування клієнта...");
                clientSocket = serverSocket.accept();
                logger.info(ServerMessages.CLIENT_ACCESSED.getMessage());

                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                answerClient("Hello!");

                String clientLine = readClient();

                boolean clientValidation = checkClientsGreetings(clientLine); // did the client say hello in Ukrainian
                if (!clientValidation) {
                    answerClient("Що таке паляниця?");
                    logger.info("Сервер відповів клієнту.");

                    clientLine = readClient();
                    boolean palianytsiaCheck = checkClientsPalianytsiaAnswer(clientLine); // did the client answer correctly
                    if (palianytsiaCheck) {
                        answerClient("Відповідь правильна! Поточна дата і час: "
                                + LocalDateTime.now() + ". До побачення!");
                    } else {
                        answerClient("Відповідь неправильна. Перериваю з'єднання.");
                    }
                } else {
                    answerClient("Вітаю і до побачення! З'єднання буде перервано.");
                    logger.info("Сервер привітав клієнта.");
                }

            } catch (IOException e) {
                logger.warning(ServerMessages.IO_EXCEPTION.getMessage());
            } finally {
                clientSocket.close();
                clientReader.close();
                writer.close();
            }
        } catch (IOException e) {
            logger.warning(ServerMessages.IO_EXCEPTION.getMessage());
        }
    }

    String readClient() throws IOException {
        System.out.println(ServerMessages.WAITING_CLIENT_RESPONSE.getMessage());
        String clientLine = clientReader.readLine();
        System.out.println(ServerMessages.CLIENT_MESSAGE.getMessage() + clientLine);
        return clientLine;
    }

    void answerClient(String serverLine) {
        writer.println(serverLine);
    }

    boolean checkClientsGreetings(String clientLine) {
        Pattern pattern = Pattern.compile("[ыэъё]");
        Matcher matcher = pattern.matcher(clientLine);
        if (matcher.find() || clientLine.equalsIgnoreCase("привет")) {
            logger.info("Клієнт привітався не українською мовою.");
            return false;
        }
        logger.info("Клієнт привітався українською мовою.");
        return true;
    }

    boolean checkClientsPalianytsiaAnswer(String clientLine) {
        if (!clientLine.equalsIgnoreCase("хліб")) {
            logger.info("Клієнт відповів неправильно, з'єднання буде перервано");
            return false;
        }
        logger.info("Клієнт відповів правильно, йому буде повідомлено поточну дату і час.");
        return true;
    }
}
