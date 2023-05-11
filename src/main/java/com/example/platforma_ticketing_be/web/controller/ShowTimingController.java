package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.service.ShowTimingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/show-timings")
public class ShowTimingController {

    private final ShowTimingService showTimingService;

    public ShowTimingController(ShowTimingService showTimingService) {
        this.showTimingService = showTimingService;
    }

    @PostMapping("/page/filter")
    public ShowTimingPageResponseDto getAllShowTimingsBySpecsPage(
            @RequestBody ShowTimingPageDto dto) {
        return this.showTimingService.findAllByPagingAndFilter(dto);
    }

    @PostMapping("/show-timing-details")
    public ShowTimingDto findVenueByShowTimingDetails(@RequestBody ShowTimingVenueDto showTimingVenueDto){
        return this.showTimingService.findShowTimingByShowTimingDetails(showTimingVenueDto.getTheatreId(), showTimingVenueDto.getMovieId(),
                showTimingVenueDto.getDay(), showTimingVenueDto.getTime());
    }

    @GetMapping("/page")
    public ShowTimingPageResponseDto getAllShowTimingsPage(
            @RequestParam int page,
            @RequestParam int size) {
        return this.showTimingService.findAllByPaging(page, size);
    }

    @GetMapping("/{id}")
    public ShowTimingDto getShowTimingById(@PathVariable("id") Long id){
        return this.showTimingService.getShowTimingById(id);
    }

    @PutMapping()
    public ShowTiming create(@RequestBody ShowTimingDto showTimingDto){
        return this.showTimingService.create(showTimingDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Long id){
        this.showTimingService.delete(id);
    }
}
