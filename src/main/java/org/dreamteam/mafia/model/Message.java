package org.dreamteam.mafia.model;

/**
 * Сообщение - набор данных, описывающих  отдельное сообщение в чате
 */
public class Message {

    private String from;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message [from=" + from + ", message=" + message + "]";
    }


}