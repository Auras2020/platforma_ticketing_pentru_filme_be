package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rating")
    private double rating;

    @Column(name = "recommended_age", nullable = false)
    private String recommendedAge;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "movie_poster")
    private byte[] poster;

    @Column(name = "poster_name")
    private String posterName;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "actors", nullable = false)
    private String actors;

    @Column(name = "director", nullable = false)
    private String director;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "note")
    private double note;

    @Column(name = "trailer_name")
    private String trailerName;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<MovieGenres> movieGenres;
}
