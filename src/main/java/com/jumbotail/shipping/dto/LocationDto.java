package com.jumbotail.shipping.dto;

import lombok.*;

/**
 * Represents a geographic location with latitude and longitude.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private Double lat;
    private Double lng;
}
