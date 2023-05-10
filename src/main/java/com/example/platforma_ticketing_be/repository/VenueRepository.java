package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.VenueDto;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {

    @Query("select v from Venue v where v.theatre.id = ?1")
    Set<Venue> getAllVenueNumbersOfGivenTheatre(Long theatreId);

    @Query("select sh from ShowTiming sh where sh.theatre.id = ?1 and sh.movie.id = ?2 and sh.time = ?3")
    List<ShowTiming> findVenueByShowTimingDetails(Long theatreId, Long movieId, String time);
}
