package com.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a Kirana store customer in the marketplace.
 * Stores location coordinates for shipping distance calculations.
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    /** Full address of the Kirana store */
    private String address;

    /** City where the store is located */
    private String city;

    /** PIN code for the store location */
    private String pincode;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
