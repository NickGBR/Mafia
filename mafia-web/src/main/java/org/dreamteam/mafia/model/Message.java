package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Сообщение - набор данных, описывающих  отдельное сообщение в чате
 */
@Getter
@Setter
public class Message {

    private String from;
    private String message;
    private String role;
    private String room;

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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;

    @Override
    public String toString() {
        return "Message [from=" + from + ", message=" + message + "]";
    }

    public void setRole(String role) {
        this.role = role;
    }

    public enum Role{
        MAFIA,
        CIVILIAN,
        HOST
    }

}