package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

public class AlreadyInRoomException extends ClientErrorException {
    public AlreadyInRoomException(ClientErrorCode code, String message) {
        super(message, code);
    }

    public AlreadyInRoomException(ClientErrorCode code) {
        super(code);
    }
}
