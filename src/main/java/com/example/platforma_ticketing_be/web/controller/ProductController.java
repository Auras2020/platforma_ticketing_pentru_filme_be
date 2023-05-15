package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.ProductDto;
import com.example.platforma_ticketing_be.dtos.ProductFilterDto;
import com.example.platforma_ticketing_be.dtos.SearchedProductDto;
import com.example.platforma_ticketing_be.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public List<ProductDto> getAllProducts(@RequestBody ProductFilterDto productFilterDto){
        return this.productService.getAllProducts(productFilterDto);
    }

    @PostMapping("/category")
    public List<ProductDto> getAllProductsByCategory(@RequestBody SearchedProductDto searchedProductDto){
        return this.productService.getAllProductsByCategory(searchedProductDto.getCategory(), searchedProductDto.getProductFilter());
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void create(@RequestPart("image") MultipartFile file, @RequestPart("product") ProductDto productDto) throws IOException {
        this.productService.create(file, productDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Long id){
        this.productService.delete(id);
    }
}
