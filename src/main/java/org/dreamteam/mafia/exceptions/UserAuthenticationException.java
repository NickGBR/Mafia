package org.dreamteam.mafia.exceptions;

import lombok.Getter;
import org.dreamteam.mafia.util.ResultCode;

/**
 * Исключение, выбрасываемое при ошибках в процессе аутентификации пользователя в системе
 */
@Getter
public class UserAuthenticationException extends ExceptionWithCode {

    public UserAuthenticationException(ResultCode code) {
        super(code);
    }

    public UserAuthenticationException(ResultCode code, String msg) {
        super(msg, code);
    }
}
