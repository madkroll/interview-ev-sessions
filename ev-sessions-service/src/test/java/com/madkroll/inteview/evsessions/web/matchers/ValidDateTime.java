package com.madkroll.inteview.evsessions.web.matchers;

import lombok.extern.log4j.Log4j2;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;

@Log4j2
public class ValidDateTime extends BaseMatcher<String> {

    @Override
    public boolean matches(final Object value) {
        try {
            LocalDateTime.parse((CharSequence) value);
            return true;
        } catch (Throwable throwable) {
            log.error("Unable to parse datetime value {}", value, throwable);
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(
                String.format("String representation of datetime value. See %s for more details", LocalDateTime.class.getCanonicalName())
        );
    }
}