package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Пользователь, восстановленый из JSON, пришедшего со стороны клиента
 */

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {

    private String login;
    private String password;
    private String passwordConfirmation;

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                '}';
    }
}


