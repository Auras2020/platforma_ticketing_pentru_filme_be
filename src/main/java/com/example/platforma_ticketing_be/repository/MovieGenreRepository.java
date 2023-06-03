package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.MovieGenres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenres, Long>, JpaSpecificationExecutor<MovieGenres> {

    void deleteMovieGenresByMovieId(Long movieId);

    List<MovieGenres> findMovieGenresByMovieId(Long movieId);
}
