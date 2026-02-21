package com.jumbotail.shipping.service;

import com.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.jumbotail.shipping.dto.ShippingCalculateRequest;
import com.jumbotail.shipping.dto.ShippingCalculateResponse;
import com.jumbotail.shipping.dto.ShippingChargeResponse;
import com.jumbotail.shipping.exception.InvalidDeliverySpeedException;
import com.jumbotail.shipping.exception.NoWarehousesAvailableException;
import com.jumbotail.shipping.exception.ResourceNotFoundException;
import com.jumbotail.shipping.model.Customer;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Seller;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.model.enums.DeliverySpeed;
import com.jumbotail.shipping.model.enums.TransportMode;
import com.jumbotail.shipping.repository.CustomerRepository;
import com.jumbotail.shipping.repository.ProductRepository;
import com.jumbotail.shipping.repository.SellerRepository;
import com.jumbotail.shipping.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for calculating shipping charges based on distance, transport mode,
 * delivery speed, and product weight. Uses Strategy pattern for extensibility.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingChargeService {

    private final WarehouseRepository warehouseRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final DistanceService distanceService;
    private final WarehouseService warehouseService;

    // Constants for shipping calculation
    private static final double STANDARD_COURIER_CHARGE = 10.0;
    private static final double EXPRESS_EXTRA_PER_KG = 1.2;

    // Distance thresholds (in km)
    private static final double AEROPLANE_MIN_DISTANCE = 500.0;
    private static final double TRUCK_MIN_DISTANCE = 100.0;

    // Rate per km per kg (in Rs)
    private static final double AEROPLANE_RATE = 1.0;
    private static final double TRUCK_RATE = 2.0;
    private static final double MINI_VAN_RATE = 3.0;

    /**
     * Calculate shipping charge from a warehouse to a customer.
     * Result is cached using warehouse ID, customer ID, and delivery speed.
     *
     * @param warehouseId   ID of the warehouse
     * @param customerId    ID of the customer
     * @param deliverySpeed Delivery speed (STANDARD or EXPRESS)
     * @return ShippingChargeResponse with calculated charge and details
     */
    @Cacheable(value = "shippingCharge", key = "#warehouseId + '-' + #customerId + '-' + #deliverySpeed")
    public ShippingChargeResponse calculateShippingCharge(Long warehouseId, Long customerId, String deliverySpeed) {
        log.info("Calculating shipping charge: warehouseId={}, customerId={}, deliverySpeed={}",
                warehouseId, customerId, deliverySpeed);

        // Validate delivery speed
        DeliverySpeed speed = validateDeliverySpeed(deliverySpeed);

        // Validate warehouse exists
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + warehouseId));

        // Validate customer exists
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        // Calculate distance
        double distance = distanceService.calculateDistance(
                warehouse.getLatitude(), warehouse.getLongitude(),
                customer.getLatitude(), customer.getLongitude()
        );

        // Determine transport mode based on distance
        TransportMode transportMode = determineTransportMode(distance);

        // For this API, we need product weight. Since the API doesn't specify product,
        // we'll use a default weight of 1 kg for now. In a real scenario, this API would
        // need a productId parameter or we'd calculate for all products.
        // For now, let's return a base charge that can be multiplied by weight.
        double baseWeightKg = 1.0;

        // Calculate shipping charge
        double charge = calculateCharge(distance, baseWeightKg, transportMode, speed);

        log.info("Shipping charge calculated: Rs {} for {} km using {}", charge, distance, transportMode);

        return ShippingChargeResponse.builder()
                .shippingCharge(Math.round(charge * 100.0) / 100.0)
                .distanceKm(Math.round(distance * 100.0) / 100.0)
                .transportMode(transportMode)
                .deliverySpeed(speed.name())
                .build();
    }

    /**
     * Calculate combined shipping charge for a seller and customer.
     * This method finds the nearest warehouse and calculates the shipping charge.
     *
     * @param request ShippingCalculateRequest containing seller, customer, and delivery speed
     * @return ShippingCalculateResponse with charge and nearest warehouse details
     */
    public ShippingCalculateResponse calculateForSellerAndCustomer(ShippingCalculateRequest request) {
        log.info("Calculating shipping for seller={}, customer={}, deliverySpeed={}",
                request.getSellerId(), request.getCustomerId(), request.getDeliverySpeed());

        // Validate delivery speed
        DeliverySpeed speed = validateDeliverySpeed(request.getDeliverySpeed());

        // Validate seller exists
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + request.getSellerId()));

        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        // Get all products for this seller
        List<Product> products = productRepository.findBySellerId(request.getSellerId());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for seller ID: " + request.getSellerId());
        }

        // For simplicity, we'll use the first product to find the nearest warehouse
        // In a real scenario, we might need to handle multiple products differently
        Product firstProduct = products.get(0);

        // Find nearest warehouse
        NearestWarehouseResponse nearestWarehouse = warehouseService.findNearestWarehouse(
                request.getSellerId(), firstProduct.getId()
        );

        // Get warehouse details
        Warehouse warehouse = warehouseRepository.findById(nearestWarehouse.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        // Calculate distance from warehouse to customer
        double distance = distanceService.calculateDistance(
                warehouse.getLatitude(), warehouse.getLongitude(),
                customer.getLatitude(), customer.getLongitude()
        );

        // Determine transport mode
        TransportMode transportMode = determineTransportMode(distance);

        // Calculate total weight of all products (for demo, using first product)
        double totalWeightKg = firstProduct.getWeightKg();

        // Calculate shipping charge
        double charge = calculateCharge(distance, totalWeightKg, transportMode, speed);

        log.info("Total shipping charge: Rs {} for {} kg over {} km", charge, totalWeightKg, distance);

        return ShippingCalculateResponse.builder()
                .shippingCharge(Math.round(charge * 100.0) / 100.0)
                .nearestWarehouse(nearestWarehouse)
                .distanceKm(Math.round(distance * 100.0) / 100.0)
                .transportMode(transportMode)
                .deliverySpeed(speed.name())
                .build();
    }

    /**
     * Determines the appropriate transport mode based on distance.
     *
     * @param distanceKm Distance in kilometers
     * @return TransportMode (AEROPLANE, TRUCK, or MINI_VAN)
     */
    private TransportMode determineTransportMode(double distanceKm) {
        if (distanceKm >= AEROPLANE_MIN_DISTANCE) {
            return TransportMode.AEROPLANE;
        } else if (distanceKm >= TRUCK_MIN_DISTANCE) {
            return TransportMode.TRUCK;
        } else {
            return TransportMode.MINI_VAN;
        }
    }

    /**
     * Calculates the total shipping charge using the Strategy pattern.
     * Formula: Standard Courier Charge + (Distance * Weight * Rate) + Express Extra
     *
     * @param distance      Distance in km
     * @param weightKg      Total weight in kg
     * @param transportMode Transport mode
     * @param deliverySpeed Delivery speed
     * @return Total shipping charge in Rs
     */
    private double calculateCharge(double distance, double weightKg, TransportMode transportMode, DeliverySpeed deliverySpeed) {
        // Get rate based on transport mode
        double ratePerKmPerKg = switch (transportMode) {
            case AEROPLANE -> AEROPLANE_RATE;
            case TRUCK -> TRUCK_RATE;
            case MINI_VAN -> MINI_VAN_RATE;
        };

        // Base shipping charge = distance * weight * rate
        double baseCharge = distance * weightKg * ratePerKmPerKg;

        // Add standard courier charge
        double totalCharge = STANDARD_COURIER_CHARGE + baseCharge;

        // Add express extra if applicable
        if (deliverySpeed == DeliverySpeed.EXPRESS) {
            double expressCharge = EXPRESS_EXTRA_PER_KG * weightKg;
            totalCharge += expressCharge;
        }

        return totalCharge;
    }

    /**
     * Validates and parses the delivery speed string.
     *
     * @param deliverySpeed String representation of delivery speed
     * @return Validated DeliverySpeed enum
     * @throws InvalidDeliverySpeedException if the delivery speed is invalid
     */
    private DeliverySpeed validateDeliverySpeed(String deliverySpeed) {
        try {
            return DeliverySpeed.valueOf(deliverySpeed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDeliverySpeedException(
                    "Invalid delivery speed: " + deliverySpeed + ". Must be STANDARD or EXPRESS"
            );
        }
    }
}
