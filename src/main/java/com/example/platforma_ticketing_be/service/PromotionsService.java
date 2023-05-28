package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.PeoplePromotionDto;
import com.example.platforma_ticketing_be.entities.PeoplePromotion;
import com.example.platforma_ticketing_be.repository.PeoplePromotionRepository;
import com.example.platforma_ticketing_be.repository.ProductsPromotionRepository;
import com.example.platforma_ticketing_be.repository.TicketsPromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PromotionsService {

    private final PeoplePromotionRepository peoplePromotionRepository;
    private final ProductsPromotionRepository productsPromotionRepository;
    private final TicketsPromotionRepository ticketsPromotionRepository;
    private final ModelMapper modelMapper;

    public PromotionsService(PeoplePromotionRepository peoplePromotionRepository,
                             ProductsPromotionRepository productsPromotionRepository,
                             TicketsPromotionRepository ticketsPromotionRepository,
                             ModelMapper modelMapper) {
        this.peoplePromotionRepository = peoplePromotionRepository;
        this.productsPromotionRepository = productsPromotionRepository;
        this.ticketsPromotionRepository = ticketsPromotionRepository;
        this.modelMapper = modelMapper;
    }

    public void createPeoplePromotion(PeoplePromotionDto peoplePromotionDto){
        this.peoplePromotionRepository.save(this.modelMapper.map(peoplePromotionDto, PeoplePromotion.class));
    }

    public PeoplePromotionDto getPeoplePromotionByShowTimingId(Long showTimingId){
        PeoplePromotion peoplePromotion = this.peoplePromotionRepository.findPeoplePromotionByShowTimingId(showTimingId);
        if(peoplePromotion != null){
            return this.modelMapper.map(peoplePromotion, PeoplePromotionDto.class);
        }
        return null;
    }
}
