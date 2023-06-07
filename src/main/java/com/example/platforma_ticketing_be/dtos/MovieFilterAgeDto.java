package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieFilterAgeDto {
    private MovieFilterDto movieFilter;
    private int age;
    private Date createdDate;
}
