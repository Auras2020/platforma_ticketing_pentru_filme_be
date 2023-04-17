package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieDto {
    private String name;
    private int recommendedAge;
    private byte[] poster;
    private String genre;
    private int duration;
    private String actors;
    private String director;
    private String synopsis;
    private Long note;
}
