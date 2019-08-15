package com.madkroll.inteview.evsessions.web.matchers;

import lombok.extern.log4j.Log4j2;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;

@Log4j2
public class DateTimeIsAfter extends BaseMatcher<String> {

    private final LocalDateTime baseDateTime;

    public DateTimeIsAfter(final LocalDateTime baseDateTime) {
        this.baseDateTime = baseDateTime;
    }

    @Override
    public boolean matches(final Object value) {
        try {
            return LocalDateTime.parse((CharSequence) value).isAfter(baseDateTime);
        } catch (Throwable throwable) {
            log.error("Unable to parse datetime value {}", value, throwable);
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(
                String.format("Datetime is expected to be after: %s", baseDateTime.toString())
        );
    }
}