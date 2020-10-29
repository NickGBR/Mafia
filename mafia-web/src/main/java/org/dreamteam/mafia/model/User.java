package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;
import org.dreamteam.mafia.entities.UserEntity;

/**
 * Описание зарегестрированного пользователя, передаваемое между сервисами и контроллерами.
 */
@Getter
@Setter
public class User {

    private String login;
    private boolean isReady;
    private String name; //У телеграм юзеров имя начинается с @t_UserName
    private Boolean isAlive;
    private CharacterEnum character;

    public User(UserEntity dao) {
        login = dao.getLogin();
        name = dao.getLogin();
        isReady = dao.getIsReady();
        isAlive = dao.getCharacterStatus().equals(CharacterStatusEnum.ALIVE);
        character = dao.getCharacter();
    }
}
