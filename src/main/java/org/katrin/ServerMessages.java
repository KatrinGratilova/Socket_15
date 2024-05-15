package org.katrin;

public enum ServerMessages{
    CLIENT_MESSAGE("Клієнт: "),
    WRITE_TO_CLIENT("Напишіть клієнту: "),
    WAITING_CLIENT_RESPONSE("Очікування повідомлення від клієнта..."),
    IO_EXCEPTION("Виникла помилка IO. Кінець програми."),
    CLIENT_ACCESSED("Клієнт був приєднаний!");

    private final String message;

    ServerMessages(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}