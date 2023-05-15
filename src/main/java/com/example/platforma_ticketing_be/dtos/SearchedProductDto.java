package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchedProductDto {
    private String category;
    private ProductFilterDto productFilter;
}
