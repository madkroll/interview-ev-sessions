package com.madkroll.inteview.evsessions.web.list;

import com.google.common.collect.ImmutableList;
import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import com.madkroll.inteview.evsessions.service.StatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ListSessionsControllerTest {

    private static final String STARTED_AT = "2019-06-23T14:34:23.001";

    private static final ChargingSession SESSION_IN_PROGRESS = new ChargingSession(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "station-id-in-progress",
            LocalDateTime.parse(STARTED_AT),
            null,
            LocalDateTime.parse(STARTED_AT),
            StatusEnum.IN_PROGRESS
    );

    private static final ChargingSession SESSION_FINISHED = new ChargingSession(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "station-id-finished",
            LocalDateTime.parse(STARTED_AT).plusMinutes(1),
            LocalDateTime.parse(STARTED_AT).plusMinutes(1).plusSeconds(1),
            LocalDateTime.parse(STARTED_AT).plusMinutes(1).plusSeconds(1),
            StatusEnum.FINISHED
    );

    private static final List<ChargingSession> STORED_SESSIONS = ImmutableList.of(
            SESSION_IN_PROGRESS,
            SESSION_FINISHED
    );

    @Mock
    private SessionStatefulService sessionService;

    @Test
    public void shouldListAllSessions() {
        given(sessionService.list()).willReturn(STORED_SESSIONS);

        final ResponseEntity<List<ListSessionsResponseItem>> resultResponse = new ListSessionsController(sessionService).list();
        assertThat(resultResponse).isNotNull();
        final List<ListSessionsResponseItem> resultResponseItems = resultResponse.getBody();
        assertThat(resultResponseItems).isNotNull();
        assertThat(resultResponseItems).isNotEmpty();

        assertThat(resultResponseItems).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        turnToResponseItem(SESSION_IN_PROGRESS),
                        turnToResponseItem(SESSION_FINISHED)
                );
    }

    private ListSessionsResponseItem turnToResponseItem(final ChargingSession session) {
        return new ListSessionsResponseItem(
                session.getId().toString(),
                session.getStationId(),
                session.getUpdatedAt().toString(),
                session.getStatus().name()
        );
    }
}