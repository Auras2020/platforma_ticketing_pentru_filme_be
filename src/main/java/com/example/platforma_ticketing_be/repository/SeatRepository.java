/*
package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {

    @Query("select s.seat from Seat s where s.showTiming.id = ?1")
    Set<String> findSeatsByShowTimingId(Long id);

    @Query("select s from Seat s where s.user = ?1 order by s.showTiming.day, s.showTiming.time desc")
    List<Seat> findSeatByUserIdOrderedByDayAndTimeDesc(UserAccount userAccount);
}
*/
