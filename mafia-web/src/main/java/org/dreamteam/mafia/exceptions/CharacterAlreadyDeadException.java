package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

public class CharacterAlreadyDeadException extends ClientErrorException {
    public CharacterAlreadyDeadException(ClientErrorCode code, String message) {
        super(code, message);
    }

    public CharacterAlreadyDeadException(ClientErrorCode code) {
        super(code);
    }
}
