package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.ErrorResponse;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.util.ServerErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    Logger logger = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ClientErrorException.class)
    @ResponseBody
    public ErrorResponse handleClientError(ClientErrorException exception) {
        logger.debug("User request caused exception: " + exception.getCode() + ", "
                             + exception.getLocalizedMessage());
        return new ErrorResponse(exception.getCode().getValue(), exception.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ErrorResponse handleClientError(DataAccessException exception) {
        logger.error("DB caused exception: "
                             + exception.getLocalizedMessage());
        return new ErrorResponse(ServerErrorCode.DB_FAILURE.getValue(), exception.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse handleClientError(Exception exception) {
        logger.error("Internal logic caused exception: "
                             + exception.getLocalizedMessage());
        return new ErrorResponse(ServerErrorCode.SERVER_CODE_FAILURE.getValue(), exception.getLocalizedMessage());
    }
}
