package com.madkroll.inteview.evsessions.web.matchers;

import lombok.extern.log4j.Log4j2;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.UUID;

@Log4j2
public class ValidUUID extends BaseMatcher<String> {

    @Override
    public boolean matches(final Object value) {
        try {
            UUID.fromString((String) value);
            return true;
        } catch (Throwable throwable) {
            log.error("Unable to parse UUID value {}", value, throwable);
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(
                String.format("String representation of UUID value. See %s for more details", UUID.class.getCanonicalName())
        );
    }
}