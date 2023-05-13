package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.SeatDto;
import com.example.platforma_ticketing_be.dtos.ShowTimingDto;
import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.service.SeatService;
import com.lowagie.text.DocumentException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = "/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PutMapping()
    public void create(@RequestBody SeatDto seatDto) throws DocumentException, IOException {
        this.seatService.create(seatDto.getShowTiming(), seatDto.getSeats());
    }

    @GetMapping("/{id}")
    public Set<String> findSeatsByShowTiming(@PathVariable("id") Long id){
        return this.seatService.findSeatsByShowTimingId(id);
    }
}
