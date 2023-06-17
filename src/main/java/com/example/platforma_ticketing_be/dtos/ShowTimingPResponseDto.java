package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShowTimingPResponseDto {
    private List<ShowTimingDto> showTimings;
    private int totalItems;
}
