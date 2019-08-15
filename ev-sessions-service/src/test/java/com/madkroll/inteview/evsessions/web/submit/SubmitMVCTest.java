package com.madkroll.inteview.evsessions.web.submit;

import com.madkroll.inteview.evsessions.web.matchers.ValidDateTime;
import com.madkroll.inteview.evsessions.web.matchers.ValidUUID;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SubmitMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnBadRequestWhenStationIdIsMissing() throws Exception {
        final MockHttpServletRequestBuilder missingParameterRequest =
                MockMvcRequestBuilders.post("/chargingSessions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invalidParameter\": \"invalidParameterValue\"}");

        mockMvc.perform(missingParameterRequest)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldSubmitNewSession() throws Exception {
        final String stationId = "correct-station-id";

        final MockHttpServletRequestBuilder submitNewSessionRequest =
                MockMvcRequestBuilders.post("/chargingSessions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationId\": \"" + stationId + "\"}");

        mockMvc.perform(submitNewSessionRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(new ValidUUID()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stationId").value(stationId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(new ValidDateTime()))
        ;
    }
}
