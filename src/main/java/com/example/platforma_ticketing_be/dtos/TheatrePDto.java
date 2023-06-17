package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheatrePDto {
    private Long theatreId;
    private int page;
    private int size;
}
