package org.dreamteam.mafia.exceptions;

/**
 *  Исключение, выбрасываемое при ошибках в процессе регистрации пользователя
 */
public class UserRegistrationException extends Exception {

    UserRegistrationException() {
        super();
    }

    UserRegistrationException(String msg) {
        super(msg);
    }
}
