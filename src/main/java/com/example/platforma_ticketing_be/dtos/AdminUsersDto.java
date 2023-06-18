package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminUsersDto {
    private UserFilterDto userFilterDto;
    private UserPDto dto;
}
