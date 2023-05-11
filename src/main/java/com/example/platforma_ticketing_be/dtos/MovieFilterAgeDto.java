package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieFilterAgeDto {
    private MovieFilterDto movieFilter;
    private int age;
}
