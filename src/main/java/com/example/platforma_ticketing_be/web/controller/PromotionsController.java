package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.PeoplePromotionDto;
import com.example.platforma_ticketing_be.dtos.ReviewDto;
import com.example.platforma_ticketing_be.service.PromotionsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/promotions")
public class PromotionsController {

    private final PromotionsService promotionsService;

    public PromotionsController(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }

    @PutMapping("/people")
    public void createPeoplePromotion(@RequestBody PeoplePromotionDto peoplePromotionDto){
        this.promotionsService.createPeoplePromotion(peoplePromotionDto);
    }

    @GetMapping("/people/{id}")
    PeoplePromotionDto getPeoplePromotionByShowTimingId(@PathVariable("id") Long id){
        return this.promotionsService.getPeoplePromotionByShowTimingId(id);
    }
}
