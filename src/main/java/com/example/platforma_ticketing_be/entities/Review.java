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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "note", nullable = false)
    private int note;

    @Column(name = "opinion", nullable = false)
    private String opinion;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @Column(name="anonymous", nullable=false)
    private boolean anonymous;
}
