package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.UserRole;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFilterDto {
    private String name;
    private String email;
    private String role;
    private String ageInterval;
    private String searchString;
}
