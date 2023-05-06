package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreDto {
    private Long id;
    private String name;
    private byte[] poster;
    private String posterName;
    private String location;
    private String address;
    private String email;
    private String phone;
}
