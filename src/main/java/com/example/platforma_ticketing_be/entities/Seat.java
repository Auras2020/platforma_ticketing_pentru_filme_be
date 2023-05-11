package com.example.platforma_ticketing_be.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Seat {

    public Seat(ShowTiming showTiming, String seat) {
        this.showTiming = showTiming;
        this.seat = seat;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="show_timing_id", nullable=false)
    private ShowTiming showTiming;

    @Column(name = "seat", nullable = false)
    private String seat;

    public Seat() {

    }
}
