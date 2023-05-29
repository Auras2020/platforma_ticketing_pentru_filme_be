package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.ShowTiming;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketsPromotionDto {
    private Long id;
    private int nrTickets;
    private int reduction;
    private ShowTimingDto showTiming;
}
