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
public class RegistrationDTO implements Serializable {

    private String login;
    private String password;
    private String passwordConfirmation;

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                '}';
    }

    public LoginDTO getLoginData() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin(this.login);
        loginDTO.setPassword(this.password);
        return loginDTO;
    }
}


