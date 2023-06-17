package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreManagerMoviesDto {
    private MovieFilterDto movieFilterDto;
    private TheatrePDto theatreDto;
}
