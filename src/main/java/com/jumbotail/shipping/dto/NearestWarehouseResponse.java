package com.jumbotail.shipping.dto;

import lombok.*;

/**
 * Response DTO for the nearest warehouse API.
 * Returns the warehouse ID and its geographic coordinates.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearestWarehouseResponse {
    private Long warehouseId;
    private String warehouseName;
    private LocationDto warehouseLocation;
    private Double distanceKm;
}
