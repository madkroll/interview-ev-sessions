package com.madkroll.inteview.evsessions.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionStatefulService {

    private final Map<String, ChargingSession> sessions;

    public SessionStatefulService(final Map<String, ChargingSession> sessions) {
        this.sessions = sessions;
    }

    public ChargingSession submit(final String stationId) {
        final LocalDateTime createdAt = LocalDateTime.now();

        final ChargingSession session = new ChargingSession(
                UUID.randomUUID(),
                stationId,
                createdAt,
                null,
                createdAt,
                StatusEnum.IN_PROGRESS
        );

        sessions.put(session.getId().toString(), session);

        return session;
    }

    public ChargingSession finish(final String sessionId) {
        final ChargingSession sessionToFinish = sessions.get(sessionId);

        if (sessionToFinish == null) {
            throw new SessionNotFoundException(String.format("Session does not exist %s", sessionId));
        }

        if (sessionToFinish.getStatus() == StatusEnum.FINISHED) {
            throw new SessionNotFoundException(String.format("Session has been already finished %s", sessionId));
        }

        final LocalDateTime finishedAt = LocalDateTime.now();

        final ChargingSession finishedSession = new ChargingSession(
                sessionToFinish.getId(),
                sessionToFinish.getStationId(),
                sessionToFinish.getStartedAt(),
                finishedAt,
                finishedAt,
                StatusEnum.FINISHED
        );

        sessions.put(
                sessionId,
                finishedSession
        );

        return finishedSession;
    }
}