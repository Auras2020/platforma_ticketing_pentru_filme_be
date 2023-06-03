package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieDto {
    private Long id;
    private String name;
    private double rating;
    private String recommendedAge;
    private byte[] poster;
    private String posterName;
    private Set<GenreDto> genres;
    private int duration;
    private String actors;
    private String director;
    private String synopsis;
    private double note;
    private String trailerName;
}
