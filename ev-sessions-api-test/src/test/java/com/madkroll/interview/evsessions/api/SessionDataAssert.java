package com.madkroll.interview.evsessions.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class SessionDataAssert {

    private static final Logger log = LoggerFactory.getLogger(SessionDataAssert.class);

    public static boolean isValidDateTime(final String dateTimeAsString) {
        try {
            LocalDateTime.parse(dateTimeAsString);
            return true;
        } catch (final DateTimeParseException e) {
            log.error("Unable to parse datetime: {}", dateTimeAsString, e);
            return false;
        }
    }

    public static boolean isPositive(final Integer value) {
        return value > 0;
    }

    public static boolean summaryIsCorrect(final int totalCount, final int startedCount, final int stoppedCount) {
        return totalCount >= 0 && startedCount >= 0 && stoppedCount >= 0
                && totalCount == startedCount + stoppedCount;
    }
}