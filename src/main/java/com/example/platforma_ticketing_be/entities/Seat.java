package com.example.platforma_ticketing_be.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="show_timing_id", nullable=false)
    private ShowTiming showTiming;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserAccount user;

    @Column(name = "seat", nullable = false)
    private String seat;

    @Column(name = "status", nullable = false)
    private String status; //[bought, reserved, cancelled]

    public Seat(ShowTiming showTiming, String seat, UserAccount user) {
        this.showTiming = showTiming;
        this.seat = seat;
        this.user = user;
    }

    public Seat() {

    }
}
