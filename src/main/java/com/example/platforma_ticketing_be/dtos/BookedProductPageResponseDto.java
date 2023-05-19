package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.BookedProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookedProductPageResponseDto {
    private List<BookedProductsDto> bookedProducts;
    //private int currentPage;
    private int totalItems;
    //private int totalPages;
}
