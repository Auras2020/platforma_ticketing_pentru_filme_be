package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderFilterDto {
    private String theatreLocation;
    private String theatreName;
    private String movieName;
    private Date day;
    private String productName;
    private String ticketStatus;
    private String productStatus;
    private String searchString;
}
