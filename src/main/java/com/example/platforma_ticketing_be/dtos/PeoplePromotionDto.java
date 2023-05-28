package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.ShowTiming;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PeoplePromotionDto {
    private Long id;
    private int adult;
    private int student;
    private int child;
    private ShowTimingDto showTiming;
}
