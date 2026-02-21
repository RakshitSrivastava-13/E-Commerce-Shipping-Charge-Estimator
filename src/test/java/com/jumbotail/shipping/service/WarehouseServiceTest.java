package com.jumbotail.shipping.service;

import com.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.jumbotail.shipping.exception.NoWarehousesAvailableException;
import com.jumbotail.shipping.exception.ResourceNotFoundException;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Seller;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.repository.ProductRepository;
import com.jumbotail.shipping.repository.SellerRepository;
import com.jumbotail.shipping.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link WarehouseService}.
 */
@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DistanceService distanceService;

    @InjectMocks
    private WarehouseService warehouseService;

    private Seller seller;
    private Product product;
    private Warehouse warehouse1;
    private Warehouse warehouse2;

    @BeforeEach
    void setUp() {
        seller = Seller.builder()
                .id(1L)
                .name("Test Seller")
                .latitude(12.9716)
                .longitude(77.5946)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .weightKg(1.0)
                .seller(seller)
                .build();

        warehouse1 = Warehouse.builder()
                .id(1L)
                .name("Warehouse 1")
                .latitude(13.0)
                .longitude(77.6)
                .build();

        warehouse2 = Warehouse.builder()
                .id(2L)
                .name("Warehouse 2")
                .latitude(19.0)
                .longitude(72.8)
                .build();
    }

    @Test
    void testFindNearestWarehouse_Success() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse1, warehouse2));
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0)  // warehouse1 closer
                .thenReturn(100.0); // warehouse2 farther

        NearestWarehouseResponse response = warehouseService.findNearestWarehouse(1L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getWarehouseId());
        assertEquals("Warehouse 1", response.getWarehouseName());
        verify(warehouseRepository, times(1)).findAll();
    }

    @Test
    void testFindNearestWarehouse_SellerNotFound_ThrowsException() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                warehouseService.findNearestWarehouse(999L, 1L)
        );
    }

    @Test
    void testFindNearestWarehouse_ProductNotFound_ThrowsException() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                warehouseService.findNearestWarehouse(1L, 999L)
        );
    }

    @Test
    void testFindNearestWarehouse_NoWarehouses_ThrowsException() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NoWarehousesAvailableException.class, () ->
                warehouseService.findNearestWarehouse(1L, 1L)
        );
    }
}
