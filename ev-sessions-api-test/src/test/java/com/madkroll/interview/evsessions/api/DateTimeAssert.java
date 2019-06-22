package com.madkroll.interview.evsessions.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateTimeAssert {

    private static final Logger log = LoggerFactory.getLogger(DateTimeAssert.class);

    public static boolean isValid(final String dateTimeAsString) {
        try {
            LocalDateTime.parse(dateTimeAsString);
            return true;
        } catch (final DateTimeParseException e) {
            log.error("Unable to parse datetime: {}", dateTimeAsString, e);
            return false;
        }
    }
}