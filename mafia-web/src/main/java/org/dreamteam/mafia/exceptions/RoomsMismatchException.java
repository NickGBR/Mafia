package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

public class RoomsMismatchException extends ClientErrorException {

    public RoomsMismatchException(ClientErrorCode code) {
        super(code);
    }

    public RoomsMismatchException(ClientErrorCode code, String message) {
        super(code, message);
    }

}
