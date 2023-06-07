package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query("select m from Movie m where m.recommendedAge in ?1")
    Set<Movie> getAllMoviesWithACertainRecommendedAge(String[] categories);

    @Query("select m from Movie m order by m.name")
    List<Movie> getAllOrderedMovies();
}
