package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreManagerDashboardDto {
    private int movies;
    private int venues;
    private int tickets;
    private int products;
}
