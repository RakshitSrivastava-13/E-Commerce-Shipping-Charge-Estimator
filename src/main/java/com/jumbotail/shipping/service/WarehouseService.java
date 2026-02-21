package com.jumbotail.shipping.service;

import com.jumbotail.shipping.dto.LocationDto;
import com.jumbotail.shipping.dto.NearestWarehouseResponse;
import com.jumbotail.shipping.exception.NoWarehousesAvailableException;
import com.jumbotail.shipping.exception.ResourceNotFoundException;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Seller;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.repository.ProductRepository;
import com.jumbotail.shipping.repository.SellerRepository;
import com.jumbotail.shipping.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for finding the nearest warehouse for a seller's product.
 * Implements caching for performance optimization using Spring Cache.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final DistanceService distanceService;

    /**
     * Finds the nearest warehouse to a seller's location.
     * Result is cached using seller ID and product ID as the cache key.
     *
     * @param sellerId  ID of the seller
     * @param productId ID of the product
     * @return NearestWarehouseResponse with warehouse details and distance
     * @throws ResourceNotFoundException if seller or product not found
     * @throws NoWarehousesAvailableException if no warehouses exist
     */
    @Cacheable(value = "nearestWarehouse", key = "#sellerId + '-' + #productId")
    public NearestWarehouseResponse findNearestWarehouse(Long sellerId, Long productId) {
        log.info("Finding nearest warehouse for sellerId={}, productId={}", sellerId, productId);

        // Validate seller exists
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        // Validate product exists  
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        // Get all warehouses
        List<Warehouse> warehouses = warehouseRepository.findAll();
        if (warehouses.isEmpty()) {
            throw new NoWarehousesAvailableException("No warehouses available to drop off products");
        }

        // Find the nearest warehouse using distance calculation
        Warehouse nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Warehouse warehouse : warehouses) {
            double distance = distanceService.calculateDistance(
                    seller.getLatitude(), seller.getLongitude(),
                    warehouse.getLatitude(), warehouse.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearest = warehouse;
            }
        }

        log.info("Nearest warehouse found: {} at distance {} km", nearest.getName(), minDistance);

        return NearestWarehouseResponse.builder()
                .warehouseId(nearest.getId())
                .warehouseName(nearest.getName())
                .warehouseLocation(LocationDto.builder()
                        .lat(nearest.getLatitude())
                        .lng(nearest.getLongitude())
                        .build())
                .distanceKm(Math.round(minDistance * 100.0) / 100.0) // Round to 2 decimals
                .build();
    }
}
