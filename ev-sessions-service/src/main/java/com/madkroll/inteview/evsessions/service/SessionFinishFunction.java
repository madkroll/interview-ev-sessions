package com.madkroll.inteview.evsessions.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

/**
 * Function replacing present in-progress session with new instance for the same session but finished.
 * */
@Service
public class SessionFinishFunction implements BiFunction<String, ChargingSession, ChargingSession> {

    @Override
    public ChargingSession apply(final String sessionId, final ChargingSession sessionToFinish) {
        if (sessionToFinish.getStatus() == StatusEnum.FINISHED) {
            throw new SessionNotFoundException(
                    String.format("Session has been already finished %s", sessionId)
            );
        }

        final LocalDateTime finishedAt = LocalDateTime.now();

        return new ChargingSession(
                sessionToFinish.getId(),
                sessionToFinish.getStationId(),
                sessionToFinish.getStartedAt(),
                finishedAt,
                finishedAt,
                StatusEnum.FINISHED
        );
    }
}