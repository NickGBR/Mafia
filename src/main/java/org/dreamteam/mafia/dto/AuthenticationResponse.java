package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Классс ответа на запрос регистрации. Сериализуется в JSON и отправляется клиенту
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private int result;
    private String answerMessage;
    private String token;
}
