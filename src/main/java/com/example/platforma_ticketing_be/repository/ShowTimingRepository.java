package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ShowTimingRepository extends JpaRepository<ShowTiming, Long>, JpaSpecificationExecutor<ShowTiming> {
    @Query("select sh from ShowTiming sh where sh.theatre.id = ?1")
    Set<ShowTiming> getAllMoviesFromATheatre(Long theatreId);

    @Query("select sh.movie from ShowTiming sh where sh.theatre.id = ?1 and sh.day = ?2")
    Set<Movie> getAllMoviesFromATheatreAtAGivenDay(Long theatreId, Date day);

    @Query("select sh.time from ShowTiming sh where sh.theatre.id = ?1 and sh.movie.id = ?2 and sh.day = ?3 order by sh.time")
    Set<String> getAllTimesByShowTiming(Long theatreId, Long movieId, Date day);

    @Query("select sh from ShowTiming sh where sh.theatre.id = ?1 and sh.movie.id = ?2 order by sh.time")
    List<ShowTiming> getAllTimesOfAMovieInADayFromATheatre(Long theatreId, Long movieId);

    @Query("select sh from ShowTiming sh where sh.theatre.id = ?1 and sh.movie.id = ?2 and sh.time = ?3")
    List<ShowTiming> findShowTimingByShowTimingDetails(Long theatreId, Long movieId, String time);
}
