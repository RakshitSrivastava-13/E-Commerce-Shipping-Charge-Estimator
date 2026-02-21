package com.jumbotail.shipping.controller;

import com.jumbotail.shipping.dto.LocationDto;
import com.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.jumbotail.shipping.exception.ResourceNotFoundException;
import com.jumbotail.shipping.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link WarehouseController}.
 */
@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @Test
    void testGetNearestWarehouse_Success() throws Exception {
        NearestWarehouseResponse response = NearestWarehouseResponse.builder()
                .warehouseId(1L)
                .warehouseName("Test Warehouse")
                .warehouseLocation(LocationDto.builder()
                        .lat(12.9716)
                        .lng(77.5946)
                        .build())
                .distanceKm(10.5)
                .build();

        when(warehouseService.findNearestWarehouse(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/warehouse/nearest")
                        .param("sellerId", "1")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouseId").value(1))
                .andExpect(jsonPath("$.warehouseName").value("Test Warehouse"));
    }

    @Test
    void testGetNearestWarehouse_MissingParameter() throws Exception {
        mockMvc.perform(get("/api/v1/warehouse/nearest")
                        .param("sellerId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetNearestWarehouse_SellerNotFound() throws Exception {
        when(warehouseService.findNearestWarehouse(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException("Seller not found"));

        mockMvc.perform(get("/api/v1/warehouse/nearest")
                        .param("sellerId", "999")
                        .param("productId", "1"))
                .andExpect(status().isNotFound());
    }
}
