package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.ShowTimingDto;
import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.repository.SeatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;

    public SeatService(SeatRepository seatRepository, ModelMapper modelMapper) {
        this.seatRepository = seatRepository;
        this.modelMapper = modelMapper;
    }

    public Seat create(ShowTimingDto showTimingDto, String seat){
        ShowTiming showTiming = this.modelMapper.map(showTimingDto, ShowTiming.class);
        Seat seat1 = new Seat(showTiming, seat);
        seatRepository.save(seat1);
        return seat1;
    }

    public Set<String> findSeatsByShowTimingId(Long id){
        return this.seatRepository.findSeatsByShowTimingId(id);
    }
}
