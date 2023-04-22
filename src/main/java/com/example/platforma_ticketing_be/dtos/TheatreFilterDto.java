package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreFilterDto {

    private String name;
    private String location;
    private String address;
    private String searchString;
}
