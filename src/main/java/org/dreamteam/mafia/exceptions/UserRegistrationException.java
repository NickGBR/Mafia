package org.dreamteam.mafia.exceptions;

import lombok.Getter;
import org.dreamteam.mafia.util.ResultCode;

/**
 *  Исключение, выбрасываемое при ошибках в процессе регистрации пользователя
 */
@Getter
public class UserRegistrationException extends Exception {

    ResultCode code;

    public  UserRegistrationException(ResultCode code) {
        super();
        this.code = code;
    }


    public UserRegistrationException(ResultCode code, String msg) {

        super(msg);
        this.code = code;
    }
}
