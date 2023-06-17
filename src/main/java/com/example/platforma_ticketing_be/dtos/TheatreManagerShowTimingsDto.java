package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatreManagerShowTimingsDto {
    private ShowTimingFilterDto showTimingFilterDto;
    private TheatrePDto theatreDto;
}
