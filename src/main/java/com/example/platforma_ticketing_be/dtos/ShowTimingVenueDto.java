package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowTimingVenueDto {
    private Long theatreId;
    private Long movieId;
    private Date day;
    private String time;
}
