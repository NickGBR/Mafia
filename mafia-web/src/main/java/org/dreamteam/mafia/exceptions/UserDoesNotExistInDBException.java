package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

public class UserDoesNotExistInDBException extends ClientErrorException {

    public UserDoesNotExistInDBException(ClientErrorCode code) {
        super(code);
    }

    public UserDoesNotExistInDBException(ClientErrorCode code, String message) {
        super(code, message);
    }
}
