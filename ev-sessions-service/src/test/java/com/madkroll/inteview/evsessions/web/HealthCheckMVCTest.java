package com.madkroll.inteview.evsessions.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"endpoints.health.time-to-live=0"})
public class HealthCheckMVCTest {

    private static final String HEALTHCHECK_ENDPOINT_URL = "/health";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldBeHealthyWhenTestChargingAvailabilityResolved() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(HEALTHCHECK_ENDPOINT_URL);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("UP")));
    }
}
