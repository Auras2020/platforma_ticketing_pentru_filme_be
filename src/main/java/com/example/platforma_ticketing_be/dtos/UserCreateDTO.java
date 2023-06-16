package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreateDTO {
    private String name;
    private int age;
    private String email;
    private String password;
    private String role;
    private TheatreDto theatre;
}
