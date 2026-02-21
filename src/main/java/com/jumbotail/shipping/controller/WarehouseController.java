package com.jumbotail.shipping.controller;

import com.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.jumbotail.shipping.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for warehouse-related operations.
 * Provides API to find the nearest warehouse for a seller's product.
 */
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Slf4j
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * GET /api/v1/warehouse/nearest - Find the nearest warehouse for a seller and product.
     *
     * @param sellerId  ID of the seller
     * @param productId ID of the product
     * @return NearestWarehouseResponse with warehouse details
     */
    @GetMapping("/nearest")
    public ResponseEntity<NearestWarehouseResponse> getNearestWarehouse(
            @RequestParam Long sellerId,
            @RequestParam Long productId) {

        log.info("Request received: GET /api/v1/warehouse/nearest?sellerId={}&productId={}", sellerId, productId);

        NearestWarehouseResponse response = warehouseService.findNearestWarehouse(sellerId, productId);

        return ResponseEntity.ok(response);
    }
}
