package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPResponseDto {
    private List<UserCreateDTO> users;
    private int totalItems;
}
