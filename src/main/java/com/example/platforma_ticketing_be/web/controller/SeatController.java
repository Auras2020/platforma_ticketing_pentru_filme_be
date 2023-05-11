package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.SeatDto;
import com.example.platforma_ticketing_be.dtos.ShowTimingDto;
import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PutMapping()
    public Seat create(@RequestBody SeatDto seatDto){
        return this.seatService.create(seatDto.getShowTiming(), seatDto.getSeat());
    }

    @GetMapping("/{id}")
    public Set<String> findSeatsByShowTiming(@PathVariable("id") Long id){
        return this.seatService.findSeatsByShowTimingId(id);
    }
}
