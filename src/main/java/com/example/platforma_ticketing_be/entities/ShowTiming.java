package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity(name = "show_schedule")
public class ShowTiming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name="theatre_id", nullable=false)
    private Theatre theatre;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "time", nullable = false)
    private String time;

    @Column(name = "day", nullable = false)
    private Date day;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name="venue_id", nullable=false)
    private Venue venue;
}
