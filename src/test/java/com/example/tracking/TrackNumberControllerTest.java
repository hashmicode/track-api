package com.example.tracking.controller;

import com.example.tracking.service.TrackNumberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * unit tests for the TrackNumberController.
 * focus on endpoint validation, HTTP status codes, and correct interaction with the service layer.
 */

@WebMvcTest(TrackNumberController.class)
public class TrackNumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackNumberService trackNumberService;

    /**
     * Test a valid request and expect HTTP 200 OK and correct JSON structure.
     */
    @Test
    void shouldReturn200ForValidRequest() throws Exception {
        Mockito.when(trackNumberService.generateTrackNumber(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn("ABCD1234EFGH5678");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/next-tracking-number")
                        .param("origin_country_id", "MY")
                        .param("destination_country_id", "ID")
                        .param("weight", "1.234")
                        .param("created_at", "2025-05-30T10:30:00+08:00")
                        .param("customer_id", UUID.randomUUID().toString())
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tracking_number").value("ABCD1234EFGH5678"));
    }

    /**
     * Test invalid origin country code (more than 2 characters).
     */

    @Test
    void shouldReturn400ForInvalidCountryCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/next-tracking-number")
                        .param("origin_country_id", "MYS")
                        .param("destination_country_id", "ID")
                        .param("weight", "1.234")
                        .param("created_at", "2025-05-30T10:30:00+08:00")
                        .param("customer_id", UUID.randomUUID().toString())
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test invalid weight (negative value).
     */
    @Test
    void shouldReturn400ForInvalidWeight() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/next-tracking-number")
                        .param("origin_country_id", "MY")
                        .param("destination_country_id", "ID")
                        .param("weight", "-5.234")
                        .param("created_at", "2025-05-30T10:30:00+08:00")
                        .param("customer_id", UUID.randomUUID().toString())
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test invalid customer slug (bad characters).
     */
    @Test
    void shouldReturn400ForInvalidSlug() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/next-tracking-number")
                        .param("origin_country_id", "MY")
                        .param("destination_country_id", "ID")
                        .param("weight", "1.234")
                        .param("created_at", "2025-05-30T10:30:00+08:00")
                        .param("customer_id", UUID.randomUUID().toString())
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "RedBox--!!"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test invalid timestamp (malformed string).
     */
    @Test
    void shouldReturn400ForInvalidTimestamp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/next-tracking-number")
                        .param("origin_country_id", "MY")
                        .param("destination_country_id", "ID")
                        .param("weight", "1.234")
                        .param("created_at", "invalid-date")
                        .param("customer_id", UUID.randomUUID().toString())
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics"))
                .andExpect(status().isBadRequest());
    }
}
