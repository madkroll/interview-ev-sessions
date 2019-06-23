package com.madkroll.inteview.evsessions.web.finish;

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
public class FinishSessionControllerTest {

    private static final String STARTED_AT = "2019-06-23T14:34:23.001";

    private static final ChargingSession STORED_FINISHED_SESSION = new ChargingSession(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "station-id",
            null,
            null,
            LocalDateTime.parse(STARTED_AT),
            StatusEnum.FINISHED
    );

    @Mock
    private SessionStatefulService sessionService;

    @Test
    public void finishSession() {
        given(sessionService.finish(STORED_FINISHED_SESSION.getId().toString())).willReturn(STORED_FINISHED_SESSION);

        final ResponseEntity<FinishSessionResponse> resultResponse =
                new FinishSessionController(sessionService).finishSession(STORED_FINISHED_SESSION.getId().toString());

        assertThat(resultResponse).isNotNull();
        final FinishSessionResponse finishSessionResponse = resultResponse.getBody();
        assertThat(finishSessionResponse).isNotNull();
        assertThat(finishSessionResponse.getId()).isEqualTo(STORED_FINISHED_SESSION.getId().toString());
        assertThat(finishSessionResponse.getStationId()).isEqualTo(STORED_FINISHED_SESSION.getStationId());
        assertThat(finishSessionResponse.getStatus()).isEqualTo(STORED_FINISHED_SESSION.getStatus().name());
        assertThat(finishSessionResponse.getUpdatedAt()).isEqualTo(STORED_FINISHED_SESSION.getUpdatedAt().toString());
    }
}