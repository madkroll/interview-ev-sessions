package com.madkroll.inteview.evsessions.web.submit;

import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import com.madkroll.inteview.evsessions.service.StatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SubmitSessionControllerTest {

    private static final String STATION_ID = "station-id";
    private static final String UPDATED_AT = "2019-06-23T14:34:23.001";
    public static final String SESSION_ID = "02f227a7-3657-44e8-8c28-3331fc793dbe";

    @Mock
    private SessionStatefulService sessionService;

    @Test
    public void shouldSubmitNewSession() {
        final SubmitSessionRequest submitSessionRequest = new SubmitSessionRequest(STATION_ID);
        final ChargingSession generatedSession = new ChargingSession(
                UUID.fromString(SESSION_ID),
                STATION_ID,
                null,
                null,
                LocalDateTime.parse(UPDATED_AT),
                StatusEnum.IN_PROGRESS
        );

        given(sessionService.submit(STATION_ID)).willReturn(generatedSession);

        final ResponseEntity<SubmitSessionResponse> resultResponse =
                new SubmitSessionController(sessionService).submit(submitSessionRequest);

        assertThat(resultResponse).isNotNull();
        final SubmitSessionResponse submitSessionResponse = resultResponse.getBody();
        assertThat(submitSessionResponse).isNotNull();
        assertThat(submitSessionResponse.getId()).isEqualTo(SESSION_ID);
        assertThat(submitSessionResponse.getStationId()).isEqualTo(STATION_ID);
        assertThat(submitSessionResponse.getUpdatedAt()).isEqualTo(UPDATED_AT);
    }
}