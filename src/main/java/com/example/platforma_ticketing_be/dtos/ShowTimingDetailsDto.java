package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowTimingDetailsDto {
    private String movie;
    private String theatre;
    private String time;
    private Date day;
    private int venue;
    private String category;
    private int price;
    private int row;
    private int column;
}
