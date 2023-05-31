package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PutMapping()
    public void createReview(@RequestBody ReviewDto reviewDto){
        this.reviewService.createReview(reviewDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteReview(@PathVariable("id") Long id){
        this.reviewService.deleteById(id);
    }

    @PostMapping("/filtered")
    public List<MovieReviewDto> getAllFilteredReviews(@RequestBody ReviewFilterDto reviewFilterDto){
        return this.reviewService.getAllReviews(reviewFilterDto);
    }

    @PostMapping("/filtered-by-user")
    public List<MovieReviewDto> getAllReviewsFilteredByCurrentUser(@RequestBody ReviewUserFilterDto reviewUserFilterDto){
        return this.reviewService.getAllReviewsFilteredByCurrentUser(reviewUserFilterDto.getReviewFilterDto(), reviewUserFilterDto.getUserId());
    }

    @GetMapping("/movie/{id}")
    List<ReviewDto> getAllExistingReviewsByMovieId(@PathVariable("id") Long id){
        return this.reviewService.getAllExistingReviewsByMovieId(id);
    }

    /*@GetMapping("/number-reviews")
    public Integer getNumberOfReviews(){
        return this.reviewService.getNumberOfReviews();
    }*/
}
