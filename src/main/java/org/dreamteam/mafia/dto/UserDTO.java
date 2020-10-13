package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Пользователь, восстановленый из JSON, пришедшего со стороны клиента
 */

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String login;
    private String password;
    private String passwordConfirmation;

}
