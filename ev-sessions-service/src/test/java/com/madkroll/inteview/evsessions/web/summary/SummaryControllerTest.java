package com.madkroll.inteview.evsessions.web.summary;

import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SummaryControllerTest {

    private static final int TOTAL_COUNT = 3;
    private  static final int STARTED_COUNT = 2;
    private static final int STOPPED_COUNT = 1;

    @Mock
    private SessionStatefulService sessionService;

    @Test
    public void shouldBuildSummary() {
        given(sessionService.calculateSummary())
                .willReturn(new SummaryResponse(TOTAL_COUNT, STARTED_COUNT, STOPPED_COUNT));

        final ResponseEntity<SummaryResponse> resultResponse = new SummaryController(sessionService).summary();
        assertThat(resultResponse).isNotNull();
        final SummaryResponse resultSummary = resultResponse.getBody();
        assertThat(resultSummary).isNotNull();
        assertThat(resultSummary.getTotalCount()).isEqualTo(TOTAL_COUNT);
        assertThat(resultSummary.getStartedCount()).isEqualTo(STARTED_COUNT);
        assertThat(resultSummary.getStoppedCount()).isEqualTo(STOPPED_COUNT);
    }
}