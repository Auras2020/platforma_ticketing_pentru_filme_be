package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreDayDto {
    private MovieFilterDto movieFilter;
    private Long theatreId;
    private Date day;
}
