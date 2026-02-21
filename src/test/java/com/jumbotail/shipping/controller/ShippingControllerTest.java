package com.jumbotail.shipping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumbotail.shipping.dto.*;
import com.jumbotail.shipping.model.enums.TransportMode;
import com.jumbotail.shipping.service.ShippingChargeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link ShippingController}.
 */
@WebMvcTest(ShippingController.class)
class ShippingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShippingChargeService shippingChargeService;

    @Test
    void testGetShippingCharge_Success() throws Exception {
        ShippingChargeResponse response = ShippingChargeResponse.builder()
                .shippingCharge(150.0)
                .distanceKm(50.0)
                .transportMode(TransportMode.MINI_VAN)
                .deliverySpeed("STANDARD")
                .build();

        when(shippingChargeService.calculateShippingCharge(anyLong(), anyLong(), anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/shipping-charge")
                        .param("warehouseId", "1")
                        .param("customerId", "1")
                        .param("deliverySpeed", "STANDARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shippingCharge").value(150.0))
                .andExpect(jsonPath("$.transportMode").value("MINI_VAN"));
    }

    @Test
    void testCalculateShippingCharge_Success() throws Exception {
        ShippingCalculateRequest request = ShippingCalculateRequest.builder()
                .sellerId(1L)
                .customerId(1L)
                .deliverySpeed("EXPRESS")
                .build();

        NearestWarehouseResponse warehouseResponse = NearestWarehouseResponse.builder()
                .warehouseId(1L)
                .warehouseName("Test Warehouse")
                .warehouseLocation(LocationDto.builder().lat(12.0).lng(77.0).build())
                .distanceKm(10.0)
                .build();

        ShippingCalculateResponse response = ShippingCalculateResponse.builder()
                .shippingCharge(180.0)
                .nearestWarehouse(warehouseResponse)
                .distanceKm(100.0)
                .transportMode(TransportMode.TRUCK)
                .deliverySpeed("EXPRESS")
                .build();

        when(shippingChargeService.calculateForSellerAndCustomer(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/shipping-charge/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shippingCharge").value(180.0))
                .andExpect(jsonPath("$.nearestWarehouse.warehouseId").value(1));
    }

    @Test
    void testCalculateShippingCharge_MissingFields() throws Exception {
        ShippingCalculateRequest request = ShippingCalculateRequest.builder()
                .sellerId(1L)
                // Missing customerId and deliverySpeed
                .build();

        mockMvc.perform(post("/api/v1/shipping-charge/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
