package com.example.platforma_ticketing_be.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VenuePageResponseDto {
    private List<VenueDto> venues;
    private int currentPage;
    private int totalItems;
    private int totalPages;
}
