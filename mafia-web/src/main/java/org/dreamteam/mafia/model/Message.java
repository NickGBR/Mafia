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

    /**
     * Используется для регистрации нового пользователя. Если сообщение первое, то
     * браузер меняет значение перемменой на true, и сервер понимает что нужно добавить нового пользователя.
     */
    private boolean firstMessage = false;

    public void setRole(Role role) {
        this.role = role.toString();
    }

    @Override
    public String toString() {
        return "Message [from=" + from + ", message=" + text + "]";
    }

    public enum Role {
        MAFIA,
        CIVILIAN,
        HOST
    }

}