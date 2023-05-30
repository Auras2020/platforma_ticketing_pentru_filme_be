package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.PeoplePromotionDto;
import com.example.platforma_ticketing_be.dtos.ProductsPromotionDto;
import com.example.platforma_ticketing_be.dtos.TicketsPromotionDto;
import com.example.platforma_ticketing_be.entities.PeoplePromotion;
import com.example.platforma_ticketing_be.entities.ProductsPromotion;
import com.example.platforma_ticketing_be.entities.TicketsPromotion;
import com.example.platforma_ticketing_be.repository.PeoplePromotionRepository;
import com.example.platforma_ticketing_be.repository.ProductsPromotionRepository;
import com.example.platforma_ticketing_be.repository.TicketsPromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void deletePeoplePromotion(Long id){
        this.peoplePromotionRepository.deleteById(id);
    }

    public void createTicketsPromotion(TicketsPromotionDto ticketsPromotionDto){
        this.ticketsPromotionRepository.save(this.modelMapper.map(ticketsPromotionDto, TicketsPromotion.class));
    }

    public List<TicketsPromotionDto> getTicketsPromotionByShowTimingId(Long showTimingId){
        List<TicketsPromotion> ticketsPromotions = this.ticketsPromotionRepository.findTicketsPromotionByShowTimingId(showTimingId);
        if(ticketsPromotions != null){
            return ticketsPromotions.stream()
                    .map(ticketsPromotion -> this.modelMapper.map(ticketsPromotion, TicketsPromotionDto.class))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public void deleteTicketsPromotion(Long id){
        this.ticketsPromotionRepository.deleteById(id);
    }

    public void createProductsPromotion(ProductsPromotionDto productsPromotionDto){
        this.productsPromotionRepository.save(this.modelMapper.map(productsPromotionDto, ProductsPromotion.class));
    }

    public List<ProductsPromotionDto> getProductsPromotionByShowTimingId(Long showTimingId){
        List<ProductsPromotion> productsPromotions = this.productsPromotionRepository.findProductsPromotionByShowTimingId(showTimingId);
        if(productsPromotions != null){
            return productsPromotions.stream()
                    .map(productsPromotion -> this.modelMapper.map(productsPromotion, ProductsPromotionDto.class))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public void deleteProductsPromotion(Long id){
        this.productsPromotionRepository.deleteById(id);
    }
}
