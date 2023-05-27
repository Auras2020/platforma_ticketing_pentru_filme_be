package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieReviewDto {
    private MovieDto movie;
    private List<ReviewDto> reviews;
}
