package com.jumbotail.shipping.repository;

import com.jumbotail.shipping.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Warehouse} entity CRUD operations.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
