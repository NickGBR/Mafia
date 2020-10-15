package org.dreamteam.mafia.exceptions;

import org.dreamteam.mafia.util.ResultCode;

public class ExceptionWithCode extends Exception {
    ResultCode code;

    public ExceptionWithCode(String message, ResultCode code) {
        super(message);
        this.code = code;
    }

    public ExceptionWithCode(ResultCode code) {
        super();
        this.code = code;
    }

    public org.dreamteam.mafia.util.ResultCode getCode() {
        return this.code;
    }
}
