package com.madkroll.inteview.evsessions.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChargingSession {

    private final UUID id;
    private final String stationId;
    private final LocalDateTime startedAt;
    private final LocalDateTime stoppedAt;
    private final LocalDateTime updatedAt;
    private final StatusEnum status;
}