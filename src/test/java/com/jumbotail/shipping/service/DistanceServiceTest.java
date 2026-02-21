package com.jumbotail.shipping.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DistanceService}.
 * Tests the Haversine distance calculation algorithm.
 */
@SpringBootTest
class DistanceServiceTest {

    @Autowired
    private DistanceService distanceService;

    @Test
    void testCalculateDistance_SameLocation_ReturnsZero() {
        double distance = distanceService.calculateDistance(
                12.9716, 77.5946,  // Bangalore
                12.9716, 77.5946   // Same location
        );
        assertEquals(0.0, distance, 0.01);
    }

    @Test
    void testCalculateDistance_BangaloreToMumbai_ReturnsCorrectDistance() {
        double distance = distanceService.calculateDistance(
                12.9716, 77.5946,  // Bangalore
                19.0760, 72.8777   // Mumbai
        );
        // Expected distance approximately 840 km
        assertTrue(distance > 800 && distance < 900);
    }

    @Test
    void testCalculateDistance_DelhiToKolkata_ReturnsPositive() {
        double distance = distanceService.calculateDistance(
                28.7041, 77.1025,  // Delhi
                22.5726, 88.3639   // Kolkata
        );
        assertTrue(distance > 0);
    }
}
