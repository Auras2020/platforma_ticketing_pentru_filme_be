package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.Theatre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ShowTimingRepository extends JpaRepository<ShowTiming, Long>, JpaSpecificationExecutor<ShowTiming> {
    @Query("select sh from show_schedule sh where sh.theatre.id = ?1")
    Set<ShowTiming> getAllShowTimingsFromATheatre(Long theatreId);

    @Query("select sh from show_schedule sh where sh.theatre.id = ?1")
    List<ShowTiming> findAllShowTimingsFromATheatre(Long theatreId);

    @Query("select sh.movie from show_schedule sh where sh.theatre.id = ?1 and sh.day = ?2")
    Set<Movie> getAllMoviesFromATheatreAtAGivenDay(Long theatreId, Date day);

    @Query("select sh.time from show_schedule sh where sh.theatre.id = ?1 and sh.movie.id = ?2 and sh.day = ?3 order by sh.time")
    Set<String> getAllTimesByShowTiming(Long theatreId, Long movieId, Date day);

    @Query("select sh from show_schedule sh where sh.theatre.id = ?1 and sh.movie.id = ?2 order by sh.time")
    List<ShowTiming> getAllTimesOfAMovieInADayFromATheatre(Long theatreId, Long movieId);

    @Query("select sh from show_schedule sh where sh.theatre.id = ?1 and sh.movie.id = ?2 and sh.time = ?3")
    List<ShowTiming> findShowTimingByShowTimingDetails(Long theatreId, Long movieId, String time);

    @Query("select distinct sh.movie from show_schedule sh where sh.theatre.id = ?1")
    List<Movie> countMoviesFromATheatre(Long theatreId);

    @Query("select distinct sh.movie from show_schedule sh where sh.theatre.id = :theatreId")
    List<Movie> getMoviesFromATheatre(Pageable pageable, Long theatreId);

    @Query("select distinct sh.movie from show_schedule sh where sh.theatre.id = :theatreId and sh.movie.id in :ids")
    List<Movie> getFilteredMoviesFromATheatre(Pageable pageable, Long theatreId, Set<Long> ids);

    @Query("select sh from show_schedule sh where sh.theatre.id = :theatreId")
    List<ShowTiming> getAllShowTimingsFromAGivenTheatre(Pageable pageable, Long theatreId);

    @Query("select sh from show_schedule sh where sh.theatre.id = :theatreId and sh.id in :ids")
    List<ShowTiming> getAllFilteredShowTimingsFromAGivenTheatre(Pageable pageable, Long theatreId, Set<Long> ids);
}
