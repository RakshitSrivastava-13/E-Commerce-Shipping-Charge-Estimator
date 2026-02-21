package com.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a product listed by a seller in the marketplace.
 * Contains weight and dimension attributes used for shipping charge calculation.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /** Product category (e.g., Grocery, FMCG, Beverages) */
    private String category;

    /** Selling price in Rupees */
    @Column(nullable = false)
    private Double sellingPrice;

    /** Weight in kilograms */
    @Column(nullable = false)
    private Double weightKg;

    /** Length in centimetres */
    private Double lengthCm;

    /** Width in centimetres */
    private Double widthCm;

    /** Height in centimetres */
    private Double heightCm;

    /** Stock Keeping Unit identifier */
    private String sku;

    /** The seller who owns this product */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
}
