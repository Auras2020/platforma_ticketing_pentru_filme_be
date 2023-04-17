package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreDto {
    private String name;
    private byte[] poster;
    private String location;
    private String address;
}
