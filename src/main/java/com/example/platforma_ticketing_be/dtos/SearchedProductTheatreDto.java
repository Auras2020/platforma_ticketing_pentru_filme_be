package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchedProductTheatreDto {
    private String category;
    private Long theatreId;
    private ProductFilterDto productFilter;
}
