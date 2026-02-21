package com.jumbotail.shipping.service;

import com.jumbotail.shipping.dto.ShippingChargeResponse;
import com.jumbotail.shipping.exception.InvalidDeliverySpeedException;
import com.jumbotail.shipping.exception.ResourceNotFoundException;
import com.jumbotail.shipping.model.Customer;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.model.enums.TransportMode;
import com.jumbotail.shipping.repository.CustomerRepository;
import com.jumbotail.shipping.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ShippingChargeService}.
 */
@ExtendWith(MockitoExtension.class)
class ShippingChargeServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DistanceService distanceService;

    @InjectMocks
    private ShippingChargeService shippingChargeService;

    private Warehouse warehouse;
    private Customer customer;

    @BeforeEach
    void setUp() {
        warehouse = Warehouse.builder()
                .id(1L)
                .name("Test Warehouse")
                .latitude(12.9716)
                .longitude(77.5946)
                .build();

        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .latitude(13.0)
                .longitude(77.6)
                .build();
    }

    @Test
    void testCalculateShippingCharge_StandardDelivery_Success() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(50.0); // 50 km - should use MINI_VAN

        ShippingChargeResponse response = shippingChargeService.calculateShippingCharge(1L, 1L, "STANDARD");

        assertNotNull(response);
        assertEquals(TransportMode.MINI_VAN, response.getTransportMode());
        assertEquals("STANDARD", response.getDeliverySpeed());
        assertTrue(response.getShippingCharge() > 0);
    }

    @Test
    void testCalculateShippingCharge_ExpressDelivery_Success() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(150.0); // 150 km - should use TRUCK

        ShippingChargeResponse response = shippingChargeService.calculateShippingCharge(1L, 1L, "EXPRESS");

        assertNotNull(response);
        assertEquals(TransportMode.TRUCK, response.getTransportMode());
        assertEquals("EXPRESS", response.getDeliverySpeed());
        assertTrue(response.getShippingCharge() > 0);
    }

    @Test
    void testCalculateShippingCharge_LongDistance_UsesAeroplane() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(600.0); // 600 km - should use AEROPLANE

        ShippingChargeResponse response = shippingChargeService.calculateShippingCharge(1L, 1L, "STANDARD");

        assertNotNull(response);
        assertEquals(TransportMode.AEROPLANE, response.getTransportMode());
    }

    @Test
    void testCalculateShippingCharge_InvalidDeliverySpeed_ThrowsException() {
        // No mocking needed as the exception is thrown before any repository calls

        assertThrows(InvalidDeliverySpeedException.class, () ->
                shippingChargeService.calculateShippingCharge(1L, 1L, "INVALID")
        );
    }

    @Test
    void testCalculateShippingCharge_WarehouseNotFound_ThrowsException() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                shippingChargeService.calculateShippingCharge(1L, 1L, "STANDARD")
        );
    }

    @Test
    void testCalculateShippingCharge_CustomerNotFound_ThrowsException() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                shippingChargeService.calculateShippingCharge(1L, 1L, "STANDARD")
        );
    }
}
