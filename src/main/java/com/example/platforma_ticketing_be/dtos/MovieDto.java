package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import javax.persistence.Column;

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
    private String genre;
    private int duration;
    private String actors;
    private String director;
    private String synopsis;
    private double note;
    private String trailerName;
}
