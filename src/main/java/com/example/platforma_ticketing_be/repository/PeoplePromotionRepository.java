package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.PeoplePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PeoplePromotionRepository extends JpaRepository<PeoplePromotion, Long>, JpaSpecificationExecutor<PeoplePromotion> {

    PeoplePromotion findPeoplePromotionByShowTimingId(Long showTimingId);
}
