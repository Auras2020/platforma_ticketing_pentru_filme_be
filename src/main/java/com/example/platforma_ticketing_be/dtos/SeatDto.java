package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeatDto {
    private ShowTimingDto showTiming;
    private List<String> seats;
}
