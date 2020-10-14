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
public class LoginDTO implements Serializable {

    private String login;
    private String password;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


