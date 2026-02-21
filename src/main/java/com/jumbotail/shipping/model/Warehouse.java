package com.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a marketplace warehouse located across India.
 * Sellers drop products at the nearest warehouse, from where they are shipped
 * to customers.
 */
@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    /** City where the warehouse is located */
    private String city;

    /** Maximum storage capacity in kilograms */
    private Double capacityKg;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
