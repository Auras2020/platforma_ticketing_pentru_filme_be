package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductsPromotionDto {
    private Long id;
    private int nrProducts;
    private int reduction;
    private ShowTimingDto showTiming;
}
