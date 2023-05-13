package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="theatre_id", nullable=false)
    private Theatre theatre;

    @Column(name = "venue_number", nullable = false)
    private int venueNumber;

    @Column(name = "rows_number", nullable = false)
    private int rowsNumber;

    @Column(name = "columns_number", nullable = false)
    private int columnsNumber;
}
