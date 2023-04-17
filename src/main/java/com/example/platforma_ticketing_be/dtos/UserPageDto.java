package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPageDto {
    private UserFilterDto dto;
    private int page;
    private int size;
}
