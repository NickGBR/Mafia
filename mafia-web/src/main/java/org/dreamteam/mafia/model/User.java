package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;
import org.dreamteam.mafia.dao.UserDAO;

/**
 * Пользователь - набор данных, описывающих зарегестрированного пользователя
 */
@Getter
@Setter
public class User {

    private String login;
    private boolean isStartButtonPressed = false;
    private String chatId;
    private String room;
    private String name;
    private Message.Role role;

    public User(UserDAO dao) {
        login = dao.getLogin();
    }

    public User(){
    }
}
