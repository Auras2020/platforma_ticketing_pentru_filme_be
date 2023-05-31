package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardDto {
    private int theatres;
    private int movies;
    private int users;
    private int tickets;
    private int products;
    private int reviews;
}
