package com.madkroll.inteview.evsessions.web;

import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

/**
 * Validates general service specification such as handling bad requests, missing parameters, etc.
 * */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneralMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionStatefulService sessionService;

    @Test
    public void shouldReturnNotFoundIfNoSuchEndpoint() throws Exception {
        final MockHttpServletRequestBuilder nonExistentEndpointRequest =
                MockMvcRequestBuilders.get("/non-existent-endpoint");

        mockMvc.perform(nonExistentEndpointRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnInternalServerErrorIfInternalErrorHappened() throws Exception {
        given(sessionService.list()).willThrow(new IllegalStateException("Internal Server Error"));

        final MockHttpServletRequestBuilder failedListingSessions =
                MockMvcRequestBuilders.get("/chargingSessions");

        mockMvc.perform(failedListingSessions)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
