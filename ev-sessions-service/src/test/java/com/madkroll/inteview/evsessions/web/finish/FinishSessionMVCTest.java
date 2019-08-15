package com.madkroll.inteview.evsessions.web.finish;

import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.StatusEnum;
import com.madkroll.inteview.evsessions.web.matchers.DateTimeIsAfter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FinishSessionMVCTest {

    private static final String STARTED_AT = "2019-06-23T14:34:23.001";

    private static final ChargingSession SESSION_FINISHED = new ChargingSession(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "station-id-finished",
            LocalDateTime.parse(STARTED_AT).plusMinutes(1),
            LocalDateTime.parse(STARTED_AT).plusMinutes(1).plusSeconds(1),
            LocalDateTime.parse(STARTED_AT).plusMinutes(1).plusSeconds(1),
            StatusEnum.FINISHED
    );

    private static final ChargingSession SESSION_IN_PROGRESS = new ChargingSession(
            UUID.fromString("33333333-3333-3333-3333-333333333333"),
            "station-id-in-progress",
            LocalDateTime.parse(STARTED_AT).minusSeconds(10),
            null,
            LocalDateTime.parse(STARTED_AT).minusSeconds(10),
            StatusEnum.IN_PROGRESS
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConcurrentHashMap<String, ChargingSession> sessionsPerKey = new ConcurrentHashMap<>();

    @Test
    public void shouldReturnNotFoundResponseIfNoSuchSessionStored() throws Exception {
        final String nonExistentSessionId = "11111111-1111-1111-1111-111111111111";
        sessionsPerKey.clear();

        final MockHttpServletRequestBuilder noSessionsToFinishRequest =
                MockMvcRequestBuilders.put("/chargingSessions/" + nonExistentSessionId)
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(noSessionsToFinishRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundIfSessionAlreadyFinished() throws Exception {
        sessionsPerKey.put(SESSION_FINISHED.getId().toString(), SESSION_FINISHED);

        final MockHttpServletRequestBuilder sessionAlreadyFinishedRequest =
                MockMvcRequestBuilders.put("/chargingSessions/" + SESSION_FINISHED.getId().toString())
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(sessionAlreadyFinishedRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldFinishSessionInProgress() throws Exception {
        sessionsPerKey.put(SESSION_IN_PROGRESS.getId().toString(), SESSION_IN_PROGRESS);

        final MockHttpServletRequestBuilder finishSessionInProgress =
                MockMvcRequestBuilders.put("/chargingSessions/" + SESSION_IN_PROGRESS.getId().toString())
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(finishSessionInProgress)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(SESSION_IN_PROGRESS.getId().toString()))
                .andExpect(jsonPath("$.stationId").value(SESSION_IN_PROGRESS.getStationId()))
                .andExpect(jsonPath("$.status").value(StatusEnum.FINISHED.name()))
                .andExpect(jsonPath("$.updatedAt").value(new DateTimeIsAfter(SESSION_IN_PROGRESS.getUpdatedAt())));

        final ChargingSession finishedSession = sessionsPerKey.get(SESSION_IN_PROGRESS.getId().toString());
        assertThat(finishedSession.getId()).isEqualTo(SESSION_IN_PROGRESS.getId());
        assertThat(finishedSession.getStationId()).isEqualTo(SESSION_IN_PROGRESS.getStationId());
        assertThat(finishedSession.getUpdatedAt()).isAfter(SESSION_IN_PROGRESS.getStartedAt());
        assertThat(finishedSession.getStoppedAt()).isAfter(SESSION_IN_PROGRESS.getStartedAt());
        assertThat(finishedSession.getUpdatedAt()).isEqualTo(finishedSession.getStoppedAt());
        assertThat(finishedSession.getStatus()).isEqualTo(StatusEnum.FINISHED);
    }
}
