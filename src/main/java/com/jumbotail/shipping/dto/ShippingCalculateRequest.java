package com.jumbotail.shipping.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request body DTO for the combined shipping charge calculation endpoint.
 * Accepts seller ID, customer ID, and desired delivery speed.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingCalculateRequest {

    @NotNull(message = "sellerId is required")
    private Long sellerId;

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "deliverySpeed is required (STANDARD or EXPRESS)")
    private String deliverySpeed;
}
