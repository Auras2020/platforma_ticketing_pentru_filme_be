package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    @Query("select r from Review r where r.movie.id = ?1 order by r.createdDate desc")
    List<Review> getAllExistingReviewsByMovieId(Long id);

    @Query("SELECT m, r  " +
            "FROM Movie m " +
            "LEFT JOIN Review r ON r.movie.id = m.id " +
            "WHERE m.id in :moviesIds AND (:isReviewFilterEmpty is true OR (:isReviewFilterEmpty is false AND r.id in :reviewsIds)) " +
            "ORDER BY m.note desc, r.createdDate desc")
    List<Object[]> findFilteredReviews(List<Long> moviesIds, Set<Long> reviewsIds, boolean isReviewFilterEmpty);
}
