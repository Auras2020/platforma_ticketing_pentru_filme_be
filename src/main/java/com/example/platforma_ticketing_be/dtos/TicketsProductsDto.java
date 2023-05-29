package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.UserAccount;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketsProductsDto {
    private ShowTimingDto showTiming;
    private List<String> seats;
    private List<ProductDetailsDto> productDetails;
    private UserAccount user;
    private String ticketStatus;
    private String productStatus;
    private int nrAdults;
    private int nrStudents;
    private int nrChilds;
    private float ticketsPrice;
    private int ticketsDiscount;
    private float productsPrice;
    private int productsDiscount;
}
