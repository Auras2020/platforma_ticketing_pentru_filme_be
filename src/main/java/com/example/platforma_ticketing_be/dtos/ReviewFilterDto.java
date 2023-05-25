package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewFilterDto {
    private String movieName;
    private String recommendedAge;
    private String genre;
    private String reviewName;
    private String searchString;
}
