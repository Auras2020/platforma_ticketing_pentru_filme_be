package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailsDto {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private int number;

    public ProductDetailsDto(String name, int price, int quantity, int number) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.number = number;
    }
}
