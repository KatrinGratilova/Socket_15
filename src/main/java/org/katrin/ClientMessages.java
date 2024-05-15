package org.katrin;

public enum ClientMessages {
    SERVER_MESSAGE("Сервер: "),
    WRITE_TO_SERVER("Напишіть серверу: "),
    WAITING_SERVER_RESPONSE("Очікування повідомлення від сервера..."),
    IO_EXCEPTION("Виникла помилка IO. Кінець програми."),
    SERVER_NOT_FOUND_EXCEPTION("Сервер не знайдено. Кінець програми.");

    private final String message;

    ClientMessages(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
