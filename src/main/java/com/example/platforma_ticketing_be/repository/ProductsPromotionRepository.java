package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.ProductsPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsPromotionRepository extends JpaRepository<ProductsPromotion, Long>, JpaSpecificationExecutor<ProductsPromotion> {
}
