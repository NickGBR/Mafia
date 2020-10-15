package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ClientErrorCode;

public class ClientErrorException extends Exception {
    ClientErrorCode code;

    public ClientErrorException(String message, ClientErrorCode code) {
        super(message);
        this.code = code;
    }

    public ClientErrorException(ClientErrorCode code) {
        super();
        this.code = code;
    }

    public ClientErrorCode getCode() {
        return this.code;
    }
}
