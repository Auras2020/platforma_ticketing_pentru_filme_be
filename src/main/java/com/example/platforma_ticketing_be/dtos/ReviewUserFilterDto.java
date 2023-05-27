package com.example.platforma_ticketing_be.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewUserFilterDto {
    private ReviewFilterDto reviewFilterDto;
    private Long userId;
}
