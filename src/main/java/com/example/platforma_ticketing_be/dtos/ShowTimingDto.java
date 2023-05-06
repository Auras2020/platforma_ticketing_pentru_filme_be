package com.example.platforma_ticketing_be.dtos;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowTimingDto {
    private Long id;
    private MovieDto movie;
    private TheatreDto theatre;
    private Date startDate;
    private Date endDate;
    private String time;
    private Date day;
    private int price;
}
