package com.jumbotail.shipping.controller;

import com.jumbotail.shipping.dto.ShippingCalculateRequest;
import com.jumbotail.shipping.dto.ShippingCalculateResponse;
import com.jumbotail.shipping.dto.ShippingChargeResponse;
import com.jumbotail.shipping.service.ShippingChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for shipping charge calculation operations.
 * Provides APIs to calculate shipping charges between warehouses and customers.
 */
@RestController
@RequestMapping("/api/v1/shipping-charge")
@RequiredArgsConstructor
@Slf4j
public class ShippingController {

    private final ShippingChargeService shippingChargeService;

    /**
     * GET /api/v1/shipping-charge - Calculate shipping charge from warehouse to customer.
     *
     * @param warehouseId   ID of the warehouse
     * @param customerId    ID of the customer
     * @param deliverySpeed Delivery speed (standard or express)
     * @return ShippingChargeResponse with calculated charge
     */
    @GetMapping
    public ResponseEntity<ShippingChargeResponse> getShippingCharge(
            @RequestParam Long warehouseId,
            @RequestParam Long customerId,
            @RequestParam String deliverySpeed) {

        log.info("Request received: GET /api/v1/shipping-charge?warehouseId={}&customerId={}&deliverySpeed={}",
                warehouseId, customerId, deliverySpeed);

        ShippingChargeResponse response = shippingChargeService.calculateShippingCharge(
                warehouseId, customerId, deliverySpeed
        );

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/shipping-charge/calculate - Calculate shipping charge for seller and customer.
     * This endpoint combines warehouse finding and charge calculation.
     *
     * @param request ShippingCalculateRequest with seller, customer, and delivery speed
     * @return ShippingCalculateResponse with charge and warehouse details
     */
    @PostMapping("/calculate")
    public ResponseEntity<ShippingCalculateResponse> calculateShippingCharge(
            @Valid @RequestBody ShippingCalculateRequest request) {

        log.info("Request received: POST /api/v1/shipping-charge/calculate - {}", request);

        ShippingCalculateResponse response = shippingChargeService.calculateForSellerAndCustomer(request);

        return ResponseEntity.ok(response);
    }
}
