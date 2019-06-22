package com.madkroll.inteview.evsessions.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(final String message) {
        super(message);
    }
}