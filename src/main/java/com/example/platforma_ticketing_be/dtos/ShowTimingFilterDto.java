package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowTimingFilterDto {
    private String movieName;
    private String theatreName;
    private Date startDate;
    private Date endDate;
    private Date day;
    private String searchString;
}
