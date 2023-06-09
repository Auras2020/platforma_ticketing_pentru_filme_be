package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {

    @Query("SELECT g FROM Genre g ORDER BY g.name")
    List<Genre> getAllOrderedGenres();
}
