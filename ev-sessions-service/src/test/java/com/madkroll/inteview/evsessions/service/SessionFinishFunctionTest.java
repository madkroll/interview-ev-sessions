package com.madkroll.inteview.evsessions.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class SessionFinishFunctionTest {

    private static final String STARTED_AT = "2019-06-23T14:34:23.001";

    private static final ChargingSession SESSION_IN_PROGRESS = new ChargingSession(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "station-id",
            LocalDateTime.parse(STARTED_AT),
            LocalDateTime.parse(STARTED_AT),
            null,
            StatusEnum.IN_PROGRESS
    );

    private static final ChargingSession SESSION_FINISHED = new ChargingSession(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "station-id",
            LocalDateTime.parse(STARTED_AT),
            LocalDateTime.parse(STARTED_AT).plusMinutes(10),
            LocalDateTime.parse(STARTED_AT).plusMinutes(10),
            StatusEnum.FINISHED
    );

    @Test
    public void shouldFailIfSessionIsFinished() {
        assertThatThrownBy(() -> new SessionFinishFunction().apply(SESSION_FINISHED.getId().toString(), SESSION_FINISHED))
                .hasMessage(String.format("Session has been already finished %s", SESSION_FINISHED.getId().toString()))
                .isInstanceOf(SessionNotFoundException.class);
    }

    @Test
    public void shouldReturnFinishedSession() {
        final ChargingSession finishedSession =
                new SessionFinishFunction()
                        .apply(SESSION_IN_PROGRESS.getId().toString(), SESSION_IN_PROGRESS);

        assertThat(finishedSession).isNotNull();
        assertThat(finishedSession.getId()).isEqualTo(SESSION_IN_PROGRESS.getId());
        assertThat(finishedSession.getStationId()).isEqualTo(SESSION_IN_PROGRESS.getStationId());
        assertThat(finishedSession.getStartedAt()).isEqualTo(SESSION_IN_PROGRESS.getStartedAt());
        assertThat(finishedSession.getUpdatedAt()).isEqualTo(finishedSession.getStoppedAt());
        assertThat(finishedSession.getStoppedAt()).isAfter(SESSION_IN_PROGRESS.getStartedAt());
        assertThat(finishedSession.getStatus()).isEqualTo(StatusEnum.FINISHED);
    }
}