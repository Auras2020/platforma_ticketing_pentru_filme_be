package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.BookedProduct;
import com.example.platforma_ticketing_be.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedProductRepository extends JpaRepository<BookedProduct, Long>, JpaSpecificationExecutor<BookedProduct> {
}
