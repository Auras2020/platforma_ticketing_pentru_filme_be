package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketDetailsDto {
    private Long id;
    private int venueNumber;
    private int price;
    private String seat;

    public TicketDetailsDto(int venueNumber, int price, String seat) {
        this.venueNumber = venueNumber;
        this.price = price;
        this.seat = seat;
    }
}
