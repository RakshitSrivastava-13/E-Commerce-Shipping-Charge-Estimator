package com.jumbotail.shipping.dto;

import com.jumbotail.shipping.model.enums.TransportMode;
import lombok.*;

/**
 * Response DTO for the shipping charge calculation API.
 * Returns the calculated shipping charge along with transport details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingChargeResponse {
    private Double shippingCharge;
    private Double distanceKm;
    private TransportMode transportMode;
    private String deliverySpeed;
}
