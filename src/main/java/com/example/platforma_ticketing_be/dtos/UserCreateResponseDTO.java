package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserCreateResponseDTO {

    private long id;
    private boolean isOk;
    private String message;
}
