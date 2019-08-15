package com.madkroll.inteview.evsessions.web;

import com.madkroll.inteview.evsessions.service.SessionNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Defines handling on MVC level for exception scenarios.
 * */
@Log4j2
@ResponseBody
@ControllerAdvice
public class EVSessionsExceptionHandler {

    /**
     * Handles cases when specified session does not exist.
     * */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void sessionNotFoundHandler(final SessionNotFoundException e) {
        log.error(e.getMessage(), e);
    }

    /**
     * Handles cases when API call parameters are missing.
     * */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidRequestHandler(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
    }

    /**
     * Handles all other error cases when no any other handler matched to prevent leaking internal specifics to clients.
     * */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void sessionNotFoundHandler(final Exception anyOtherException) {
        log.error(anyOtherException.getMessage(), anyOtherException);
    }
}
