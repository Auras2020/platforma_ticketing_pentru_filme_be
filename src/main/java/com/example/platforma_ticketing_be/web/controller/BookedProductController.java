package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.service.BookedProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/booked-products")
public class BookedProductController {

    private final BookedProductService bookedProductService;

    public BookedProductController(BookedProductService bookedProductService) {
        this.bookedProductService = bookedProductService;
    }

    @PostMapping("/page/filter")
    public BookedProductPageResponseDto getAllBookedProductsBySpecsPage(
            @RequestBody BookedProductPageDto dto) {
        return this.bookedProductService.getAllBookedProductsByPagingAndFilter(dto);
    }

    @GetMapping("/page")
    public BookedProductPageResponseDto getAllBookedProductsPage(
            @RequestBody BookedProductPDto dto) {
        return this.bookedProductService.getAllBookedProductsByPaging(dto);
    }
}