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
    private boolean isReady = false;
    private String id;
    private String room;
    private String name; //У телеграм юзеров имя начинается с @t_UserName
    private Message.Role role;

    public User(UserDAO dao) {
        login = dao.getLogin();
        name = dao.getLogin();
        isReady = dao.getIsReady();
    }

    public User(){

    }
}
