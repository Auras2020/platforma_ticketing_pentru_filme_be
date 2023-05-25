package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    @Query("select r from Review r where r.movie.id = ?1 order by r.createdDate desc")
    List<Review> getAllExistingReviewsByMovieId(Long id);
}
