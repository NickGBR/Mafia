package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

public class IllegalGamePhaseException extends ClientErrorException {
    public IllegalGamePhaseException(ClientErrorCode code) {
        super(code);
    }

    public IllegalGamePhaseException(ClientErrorCode code, String message) {
        super(code, message);
    }
}
