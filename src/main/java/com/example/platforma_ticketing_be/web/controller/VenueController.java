package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.Venue;
import com.example.platforma_ticketing_be.service.VenueService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/page/filter")
    public VenuePageResponseDto getAllVenuesBySpecsPage(
            @RequestBody VenuePageDto dto) {
        return this.venueService.findAllByPagingAndFilter(dto);
    }

    @GetMapping("/page")
    public VenuePageResponseDto getAllVenuesPage(
            @RequestParam int page,
            @RequestParam int size) {
        return this.venueService.findAllByPaging(page, size);
    }

    @GetMapping("/theatre/{id}")
    public Set<VenueDto> getAllVenueNumbersOfGivenTheatre(@PathVariable("id") Long theatreId){
        return this.venueService.getAllVenueNumbersOfGivenTheatre(theatreId);
    }

    @GetMapping("/{id}")
    public VenueDto getVenueById(@PathVariable("id") Long id){
        return this.venueService.getVenueById(id);
    }

    @PutMapping()
    public Venue create(@RequestBody VenueDto venueDto){
        return this.venueService.create(venueDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Long id){
        this.venueService.delete(id);
    }
}
