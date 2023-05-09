package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VenueFilterDto {
    private String location;
    private String theatreName;
    /*private String movieName;
    private Date day;*/
    private String searchString;
}
