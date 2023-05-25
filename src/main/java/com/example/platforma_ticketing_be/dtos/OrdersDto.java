package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.UserAccount;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersDto {
    private ShowTimingDto showTiming;
    private UserAccount user;
    private int ticketsCount;
    private String ticketsStatus;
    private int productsCount;
    private String productsStatus;
    private Date createdDate;
}
