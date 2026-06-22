package com.campus.foodtruck.infrastructure.adapters.out.database.repository;

import com.campus.foodtruck.infrastructure.adapters.out.database.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataProductoRepository extends JpaRepository<ProductoEntity, Long> {
}
