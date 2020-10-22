package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

/**
 * Исключение, выбрасываемое при запросе изнутри комнаты операции недопустимой во время нахождения в комнате
 */
public class AlreadyInRoomException extends ClientErrorException {
    public AlreadyInRoomException(String message) {
        super(message, ClientErrorCode.ALREADY_IN_ROOM);
    }

    public AlreadyInRoomException() {
        super(ClientErrorCode.ALREADY_IN_ROOM);
    }
}
