package com.jumbotail.shipping.repository;

import com.jumbotail.shipping.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Seller} entity CRUD operations.
 */
@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
