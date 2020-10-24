package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

/**
 * Исключение, выбрасываемое при попытке совершить действие, разрешенное только администратору комнаты, не являясь таковым
 */
public class NotEnoughRightsException extends ClientErrorException {

    public NotEnoughRightsException(String message) {
        super(ClientErrorCode.NOT_ENOUGH_RIGHTS, message);
    }

    public NotEnoughRightsException() {
        super(ClientErrorCode.NOT_ENOUGH_RIGHTS);
    }
}
