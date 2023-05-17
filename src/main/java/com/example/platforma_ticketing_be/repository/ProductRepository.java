package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select p from Product p where p.theatre.id = ?1")
    Set<Product> getAllProductsByTheatreId(Long theatreId);

    @Query("select p from Product p where p.category = ?1 and p.theatre.id = ?2")
    Set<Product> getAllProductsByCategoryAndTheatreId(String category, Long theatreId);

    @Query("select p from Product p where p.theatre.id = ?1 and p.number > 0")
    Set<Product> getAllProductsAvailableByTheatreId(Long theatreId);

    @Query("select p from Product p where p.category = ?1 and p.theatre.id = ?2 and p.number > 0")
    Set<Product> getAllProductsAvailableByCategoryAndTheatreId(String category, Long theatreId);
}
