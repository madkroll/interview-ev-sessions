package com.madkroll.inteview.evsessions.web.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.StatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ListSessionsMVCTest {

    private static final String STARTED_AT = "2019-06-23T14:34:23.001";

    private static final ChargingSession SESSION_TOO_OLD_IN_PROGRESS = new ChargingSession(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "station-id-too-old-in-progress",
            LocalDateTime.parse(STARTED_AT),
            null,
            LocalDateTime.parse(STARTED_AT),
            StatusEnum.IN_PROGRESS
    );

    private static final ChargingSession SESSION_TOO_OLD_FINISHED = new ChargingSession(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "station-id-too-old-finished",
            LocalDateTime.parse(STARTED_AT).plusMinutes(1),
            LocalDateTime.parse(STARTED_AT).plusMinutes(1).plusSeconds(1),
            LocalDateTime.parse(STARTED_AT).plusMinutes(1).plusSeconds(1),
            StatusEnum.FINISHED
    );

    private static final ChargingSession SESSION_IN_PROGRESS = new ChargingSession(
            UUID.fromString("33333333-3333-3333-3333-333333333333"),
            "station-id-in-progress",
            LocalDateTime.now().minusSeconds(10),
            null,
            LocalDateTime.now().minusSeconds(10),
            StatusEnum.IN_PROGRESS
    );

    private static final ChargingSession SESSION_FINISHED = new ChargingSession(
            UUID.fromString("44444444-4444-4444-4444-444444444444"),
            "station-id-finished",
            LocalDateTime.now().minusSeconds(9),
            LocalDateTime.now().minusSeconds(8),
            LocalDateTime.now().minusSeconds(8),
            StatusEnum.FINISHED
    );

    private static final List<ChargingSession> STORED_SESSIONS = ImmutableList.of(
            SESSION_TOO_OLD_IN_PROGRESS,
            SESSION_TOO_OLD_FINISHED,
            SESSION_IN_PROGRESS,
            SESSION_FINISHED
    );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConcurrentHashMap<String, ChargingSession> sessionsPerKey;

    @Test
    public void shouldReturnEmptyResponseIfNoSessionsStored() throws Exception {
        given(sessionsPerKey.values()).willReturn(ImmutableList.of());

        final MockHttpServletRequestBuilder emptySessionsListingRequest =
                MockMvcRequestBuilders.get("/chargingSessions")
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(emptySessionsListingRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    public void shouldReturnAllStoredSessions() throws Exception {
        given(sessionsPerKey.values()).willReturn(STORED_SESSIONS);

        final MockHttpServletRequestBuilder listingRequest =
                MockMvcRequestBuilders.get("/chargingSessions")
                        .accept(MediaType.APPLICATION_JSON);

        final byte[] responseBodyInBytes = mockMvc.perform(listingRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(STORED_SESSIONS.size()))
                .andReturn().getResponse()
                .getContentAsByteArray();

        final List<ListSessionsResponseItem> resultResponseItems =
                new ObjectMapper()
                        .readValue(responseBodyInBytes, new TypeReference<List<ListSessionsResponseItem>>() {
                        });

        assertThat(resultResponseItems)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        turnSessionToResponseItem(SESSION_FINISHED),
                        turnSessionToResponseItem(SESSION_IN_PROGRESS),
                        turnSessionToResponseItem(SESSION_TOO_OLD_FINISHED),
                        turnSessionToResponseItem(SESSION_TOO_OLD_IN_PROGRESS)
                );
    }

    private ListSessionsResponseItem turnSessionToResponseItem(final ChargingSession session) {
        return new ListSessionsResponseItem(
                session.getId().toString(),
                session.getStationId(),
                session.getUpdatedAt().toString(),
                session.getStatus().name()
        );
    }
}
