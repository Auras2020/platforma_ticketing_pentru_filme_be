package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.ReviewDto;
import com.example.platforma_ticketing_be.entities.Review;
import com.example.platforma_ticketing_be.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    public ReviewService(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    public void createReview(ReviewDto reviewDto){
        if(reviewDto.getMovie() != null){
            this.reviewRepository.save(this.modelMapper.map(reviewDto, Review.class));
        }
    }

    public void updateReview(ReviewDto reviewDto){

    }
}
