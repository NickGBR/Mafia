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
    private String text;
    private String role;
    private String room;

    public void setRole(Role role) {
        this.role = role.toString();
    }

    @Override
    public String toString() {
        return "Message [from=" + from + ", message=" + text + "]";
    }

    public enum Role{
        MAFIA,
        CIVILIAN,
        HOST
    }

}