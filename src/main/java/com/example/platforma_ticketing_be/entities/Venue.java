package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*@Column(name = "location", nullable = false)
    private String location;*/

    @ManyToOne
    @JoinColumn(name="theatre_id", nullable=false)
    private Theatre theatre;

    /*@ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @Column(name = "day", nullable = false)
    private Date day;

    @Column(name = "time", nullable = false)
    private String time;*/

    @Column(name = "venue_number", nullable = false)
    private int venueNumber;

    @Column(name = "rows_number", nullable = false)
    private int rowsNumber;

    @Column(name = "columns_number", nullable = false)
    private int columnsNumber;
}
