package com.jumbotail.shipping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Root controller for the application.
 * Provides welcome message and API information at the base URL.
 */
@RestController
@RequestMapping("/")
public class HomeController {

    /**
     * GET / - Welcome endpoint with API information.
     *
     * @return API information and available endpoints
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "E-Commerce Shipping Charge Estimator");
        response.put("version", "1.0.0");
        response.put("description", "A B2B e-commerce marketplace application for calculating shipping charges");
        response.put("status", "running");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/v1/warehouse/nearest", "Find nearest warehouse for a seller and product");
        endpoints.put("GET /api/v1/shipping-charge", "Calculate shipping charge from warehouse to customer");
        endpoints.put("POST /api/v1/shipping-charge/calculate", "Calculate combined shipping charge for seller and customer");
        endpoints.put("GET /h2-console", "Access H2 database console");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> documentation = new HashMap<>();
        documentation.put("README", "See README.md for detailed documentation");
        documentation.put("QUICKSTART", "See QUICKSTART.md for quick start guide");
        
        response.put("documentation", documentation);
        
        return ResponseEntity.ok(response);
    }
}
