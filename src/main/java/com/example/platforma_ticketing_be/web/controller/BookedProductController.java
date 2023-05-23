/*
package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.service.BookedProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/booked-products")
public class BookedProductController {

    private final BookedProductService bookedProductService;

    public BookedProductController(BookedProductService bookedProductService) {
        this.bookedProductService = bookedProductService;
    }

    @PostMapping("/page/filter")
    public OrderPageResponseDto getAllBookedProductsBySpecsPage(
            @RequestBody BookedProductPageDto dto) {
        return this.bookedProductService.getAllBookedProductsByPagingAndFilter(dto);
    }

    @GetMapping("/page")
    public OrderPageResponseDto getAllBookedProductsPage(
            @RequestBody BookedProductPDto dto) {
        return this.bookedProductService.getAllBookedProductsByPaging(dto);
    }

    @PostMapping("/status")
    public void getAllBookedProductsPage(
            @RequestBody BookedProductsDto dto) {
        this.bookedProductService.changeBookedProductsStatus(dto);
    }

    @PostMapping("/details")
    public List<ProductDetailsDto> getBookedProductsDetails(@RequestBody BookedProductsDto bookedProductsDto){
        return this.bookedProductService.getBookedProductsDetails(bookedProductsDto);
    }
}
*/
