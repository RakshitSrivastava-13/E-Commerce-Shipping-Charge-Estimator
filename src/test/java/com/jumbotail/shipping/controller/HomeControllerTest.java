package com.jumbotail.shipping.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link HomeController}.
 */
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHome_ReturnsWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("E-Commerce Shipping Charge Estimator"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.status").value("running"))
                .andExpect(jsonPath("$.endpoints").exists())
                .andExpect(jsonPath("$.documentation").exists());
    }

    @Test
    void testHome_ContainsEndpointInformation() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endpoints['GET /api/v1/warehouse/nearest']").exists())
                .andExpect(jsonPath("$.endpoints['GET /api/v1/shipping-charge']").exists())
                .andExpect(jsonPath("$.endpoints['POST /api/v1/shipping-charge/calculate']").exists());
    }
}
