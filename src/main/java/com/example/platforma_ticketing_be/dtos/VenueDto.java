package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.Theatre;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VenueDto {
    private Long id;
    private TheatreDto theatre;
    private int venueNumber;
    private int rowsNumber;
    private int columnsNumber;
}
