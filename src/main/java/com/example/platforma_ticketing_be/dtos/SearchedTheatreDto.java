package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchedTheatreDto {
    private Long theatreId;
    private ProductFilterDto productFilter;
}
