package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDto {
    private Long id;
    private String name;
    private Date createdDate;
    private UserAccount user;
    private MovieDto movie;
}
