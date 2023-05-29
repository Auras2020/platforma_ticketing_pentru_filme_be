package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.PeoplePromotion;
import com.example.platforma_ticketing_be.entities.TicketsPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketsPromotionRepository extends JpaRepository<TicketsPromotion, Long>, JpaSpecificationExecutor<TicketsPromotion> {

    List<TicketsPromotion> findTicketsPromotionByShowTimingId(Long showTimingId);
}
