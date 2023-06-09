package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieFilterDto {

    private String name;
    private String recommendedAge;
    private String[] genre;
    private String duration;
    private String actors;
    private String director;
    private String synopsis;
    private String searchString;

    public MovieFilterDto(String name, String recommendedAge, String[] genre, String searchString) {
        this.name = name;
        this.recommendedAge = recommendedAge;
        this.genre = genre;
        this.searchString = searchString;
    }
}
