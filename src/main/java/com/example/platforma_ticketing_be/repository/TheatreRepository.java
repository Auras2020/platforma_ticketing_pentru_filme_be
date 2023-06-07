package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long>, JpaSpecificationExecutor<Theatre> {
    @Query("select distinct t.location from Theatre t")
    List<String> getAllTheatresLocations();

    List<Theatre> getDistinctByLocationOrderByName(String location);

    @Query("SELECT t FROM Theatre t ORDER BY t.location, t.name")
    List<Theatre> getAllOrderedTheatres();
}
