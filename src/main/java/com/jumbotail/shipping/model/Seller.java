package com.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a seller (supplier) in the marketplace.
 * Sellers can list multiple products and are located anywhere in India.
 */
@Entity
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    /** Business/company name */
    private String businessName;

    /** City where the seller operates from */
    private String city;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
