package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VenuePageDto {
    private VenueFilterDto dto;
    private int page;
    private int size;
}
