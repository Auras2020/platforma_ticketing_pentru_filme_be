package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private String name;
    private byte[] image;
    private String imageName;
    private String category;
    private int price;
    private int quantity;
    private int number;
}
