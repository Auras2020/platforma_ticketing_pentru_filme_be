package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatrePageResponseDto {

    private List<TheatreDto> theatres;
    private int currentPage;
    private int totalItems;
    private int totalPages;
}
