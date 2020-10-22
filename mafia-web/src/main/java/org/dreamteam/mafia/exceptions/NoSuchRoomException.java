package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

/**
 * Исключение, выбрасываемое при попытке обратиться к несуществующей комнате
 */
public class NoSuchRoomException extends ClientErrorException {

    public NoSuchRoomException(ClientErrorCode code, String message) {
        super(message, code);
    }

    public NoSuchRoomException(ClientErrorCode code) {
        super(code);
    }
}
