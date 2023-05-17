package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.UserAccount;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookedProductsDto {
    private ShowTimingDto showTiming;
    private UserAccount user;
    private List<ProductDetailsDto> productDetails;
    private String status;
}
