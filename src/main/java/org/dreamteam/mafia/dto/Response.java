package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.util.ResultCode;

/**
 * Классс ответа на запрос регистрации. Сериализуется в JSON и отправляется клиенту
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private ResultCode result;
    private String answerMessage;
}
