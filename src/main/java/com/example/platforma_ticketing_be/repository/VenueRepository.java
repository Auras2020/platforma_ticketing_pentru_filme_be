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
}
