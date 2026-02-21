package com.jumbotail.shipping.dto;

import com.jumbotail.shipping.model.enums.TransportMode;
import lombok.*;

/**
 * Response DTO for the combined shipping charge calculation endpoint.
 * Includes the shipping charge plus nearest warehouse details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingCalculateResponse {
    private Double shippingCharge;
    private NearestWarehouseResponse nearestWarehouse;
    private Double distanceKm;
    private TransportMode transportMode;
    private String deliverySpeed;
}
