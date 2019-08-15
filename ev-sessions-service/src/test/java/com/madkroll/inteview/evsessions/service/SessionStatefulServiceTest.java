package com.madkroll.inteview.evsessions.service;

import com.google.common.collect.ImmutableSet;
import com.madkroll.inteview.evsessions.web.summary.SummaryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SessionStatefulServiceTest {

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
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "station-id",
            LocalDateTime.parse(STARTED_AT),
            LocalDateTime.parse(STARTED_AT).plusMinutes(10),
            LocalDateTime.parse(STARTED_AT).plusMinutes(10),
            StatusEnum.FINISHED
    );

    @Mock
    private ConcurrentHashMap<String, ChargingSession> sessionsPerKey;

    @Mock
    private BiFunction<String, ChargingSession, ChargingSession> finishSession;

    @Test
    public void shouldSubmitNewSession() {
        final String stationId = "station-id-submit";
        final LocalDateTime beforeSubmit = LocalDateTime.now();

        final ChargingSession resultSession = new SessionStatefulService(sessionsPerKey, finishSession).submit(stationId);

        assertThat(resultSession.getId()).isNotNull();
        assertThat(resultSession.getStationId()).isEqualTo(stationId);
        assertThat(resultSession.getStartedAt()).isNotNull();
        assertThat(resultSession.getStartedAt()).isEqualTo(resultSession.getUpdatedAt());
        assertThat(resultSession.getStoppedAt()).isNull();
        assertThat(resultSession.getStatus()).isEqualTo(StatusEnum.IN_PROGRESS);

        verify(sessionsPerKey).put(any(), any(ChargingSession.class));
    }

    @Test
    public void shouldFailOnFinishIfSessionInProgressDoesNotExist() {
        given(sessionsPerKey.computeIfPresent(SESSION_IN_PROGRESS.getId().toString(), finishSession))
                .willReturn(null);

        assertThatThrownBy(() -> new SessionStatefulService(sessionsPerKey, finishSession).finish(SESSION_IN_PROGRESS.getId().toString()))
                .isInstanceOf(SessionNotFoundException.class)
                .hasMessage(String.format("Session does not exist %s", SESSION_IN_PROGRESS.getId().toString()));
    }

    @Test
    public void shouldFinishSessionInProgress() {
        given(sessionsPerKey.computeIfPresent(SESSION_IN_PROGRESS.getId().toString(), finishSession))
                .willReturn(SESSION_FINISHED);

        final ChargingSession finishedSession =
                new SessionStatefulService(sessionsPerKey, finishSession)
                        .finish(SESSION_IN_PROGRESS.getId().toString());

        assertThat(finishedSession).isNotNull();
        assertThat(finishedSession).isEqualTo(SESSION_FINISHED);
    }

    @Test
    public void shouldListAllSessions() {
        given(sessionsPerKey.values()).willReturn(ImmutableSet.of(SESSION_IN_PROGRESS, SESSION_FINISHED));
        final List<ChargingSession> listedSessions = new SessionStatefulService(sessionsPerKey, finishSession).list();
        assertThat(listedSessions).containsExactlyInAnyOrder(SESSION_FINISHED, SESSION_IN_PROGRESS);
    }

    @Test
    public void shouldReturnSummaryForLastMinute() {
        final Set<ChargingSession> lastMinuteSessions = ImmutableSet.of(
                new ChargingSession(UUID.randomUUID(), "station-id", LocalDateTime.now(), null, LocalDateTime.now(), StatusEnum.IN_PROGRESS),
                new ChargingSession(UUID.randomUUID(), "station-id", LocalDateTime.now().minusMinutes(5), LocalDateTime.now(), LocalDateTime.now(), StatusEnum.FINISHED)
        );

        final Set<ChargingSession> tooOldSessions = ImmutableSet.of(
                new ChargingSession(UUID.randomUUID(), "station-id", LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(10), StatusEnum.IN_PROGRESS),
                new ChargingSession(UUID.randomUUID(), "station-id", LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(10), StatusEnum.FINISHED)
        );

        final Set<ChargingSession> allSessions =
                ImmutableSet.<ChargingSession>builder()
                        .addAll(lastMinuteSessions)
                        .addAll(tooOldSessions)
                        .build();

        final ConcurrentHashMap<String, ChargingSession> storedSessions = new ConcurrentHashMap<>(
                allSessions.stream().collect(Collectors.toMap(session -> session.getId().toString(), session -> session))
        );

        final SummaryResponse summaryResponse = new SessionStatefulService(storedSessions, finishSession).calculateSummary();
        assertThat(summaryResponse).isNotNull();
        assertThat(summaryResponse.getTotalCount()).isEqualTo(2);
        assertThat(summaryResponse.getStoppedCount()).isEqualTo(1);
        assertThat(summaryResponse.getStartedCount()).isEqualTo(1);
    }

    @Test
    public void shouldZeroIfEmpty() {
        final ConcurrentHashMap<String, ChargingSession> storedSessions = new ConcurrentHashMap<>();

        final SummaryResponse summaryResponse = new SessionStatefulService(storedSessions, finishSession).calculateSummary();
        assertThat(summaryResponse).isNotNull();
        assertThat(summaryResponse.getTotalCount()).isEqualTo(0);
        assertThat(summaryResponse.getStoppedCount()).isEqualTo(0);
        assertThat(summaryResponse.getStartedCount()).isEqualTo(0);
    }
}