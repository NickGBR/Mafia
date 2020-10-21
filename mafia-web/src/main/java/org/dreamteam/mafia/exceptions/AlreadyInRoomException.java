package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

/**
 * Исключение, выбрасываемое при запросе изнутри комнаты операции недопустимой во время нахождения в комнате
 */
public class AlreadyInRoomException extends ClientErrorException {
    public AlreadyInRoomException(ClientErrorCode code, String message) {
        super(message, code);
    }

    public AlreadyInRoomException(ClientErrorCode code) {
        super(code);
    }
}
