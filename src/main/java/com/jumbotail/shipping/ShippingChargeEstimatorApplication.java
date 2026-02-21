package com.jumbotail.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point for the E-Commerce Shipping Charge Estimator application.
 * <p>
 * This Spring Boot application provides REST APIs to calculate shipping charges
 * for a B2B e-commerce marketplace serving Kirana stores.
 * </p>
 */
@SpringBootApplication
@EnableCaching
public class ShippingChargeEstimatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingChargeEstimatorApplication.class, args);
    }
}
