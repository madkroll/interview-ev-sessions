package com.madkroll.inteview.evsessions.service;

import com.google.common.collect.ImmutableList;
import com.madkroll.inteview.evsessions.web.summary.SummaryResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SessionStatefulService {

    private final ConcurrentHashMap<String, ChargingSession> sessions;

    public SessionStatefulService(final ConcurrentHashMap<String, ChargingSession> sessions) {
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

    public List<ChargingSession> list() {
        return ImmutableList.copyOf(sessions.values());
    }

    public SummaryResponse calculateSummary() {
        final LocalDateTime minuteAgo = LocalDateTime.now().minusMinutes(1);

        final List<ChargingSession> sinceMinuteAgoSessions =
                sessions.values()
                        .stream()
                        .filter(chargingSession -> chargingSession.getUpdatedAt().isAfter(minuteAgo))
                        .collect(Collectors.toList());

        final int total = sinceMinuteAgoSessions.size();
        final int inProgress =
                (int) sinceMinuteAgoSessions
                        .stream()
                        .filter(chargingSession -> chargingSession.getStatus() == StatusEnum.IN_PROGRESS)
                        .count();
        final int finished = total - inProgress;

        return new SummaryResponse(total, inProgress, finished);
    }
}