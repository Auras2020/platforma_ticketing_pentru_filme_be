package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.PeoplePromotionDto;
import com.example.platforma_ticketing_be.dtos.ProductsPromotionDto;
import com.example.platforma_ticketing_be.dtos.TicketsPromotionDto;
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

    @PutMapping("/tickets")
    public void createTicketsPromotion(@RequestBody TicketsPromotionDto ticketsPromotionDto){
        this.promotionsService.createTicketsPromotion(ticketsPromotionDto);
    }

    @GetMapping("/tickets/{id}")
    List<TicketsPromotionDto> getTicketsPromotionByShowTimingId(@PathVariable("id") Long id){
        return this.promotionsService.getTicketsPromotionByShowTimingId(id);
    }

    @PutMapping("/products")
    public void createProductsPromotion(@RequestBody ProductsPromotionDto productsPromotionDto){
        this.promotionsService.createProductsPromotion(productsPromotionDto);
    }

    @GetMapping("/products/{id}")
    List<ProductsPromotionDto> getProductsPromotionByShowTimingId(@PathVariable("id") Long id){
        return this.promotionsService.getProductsPromotionByShowTimingId(id);
    }
}
