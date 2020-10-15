package org.dreamteam.mafia.exceptions;

import lombok.Getter;
import org.dreamteam.mafia.util.ResultCode;

/**
 * Исключение, выбрасываемое при ошибках в процессе регистрации пользователя
 */
@Getter
public class UserRegistrationException extends ExceptionWithCode {

    public UserRegistrationException(ResultCode code) {
        super(code);
    }

    public UserRegistrationException(ResultCode code, String msg) {
        super(msg, code);
    }
}
