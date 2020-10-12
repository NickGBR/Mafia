package org.dreamteam.mafia.model;

/**
 * Сообщение - набор данных, описывающих  отдельное сообщение в чате
 */
public class Message {

    private String from;
    private String message;
    private String role;
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

    public void setRole(Role role) {
        this.role = role.toString();
    }

    public String getRole() {
        return role;
    }

    public enum Role{
        MAFIA,
        CIVILIAN
    }

}