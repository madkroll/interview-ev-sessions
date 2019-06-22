package com.madkroll.inteview.evsessions.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionStatefulService {

    private final Map<String, ChargingSession> sessions = new ConcurrentHashMap<>();

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

        sessions.put(session.getStationId(), session);

        return session;
    }
}