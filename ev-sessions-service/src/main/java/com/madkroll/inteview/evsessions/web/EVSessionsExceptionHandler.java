package com.madkroll.inteview.evsessions.web;

import com.madkroll.inteview.evsessions.service.SessionNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseBody
@ControllerAdvice
public class EVSessionsExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void sessionNotFoundHandler(final SessionNotFoundException e) {
        log.error(e.getMessage(), e);
    }

}