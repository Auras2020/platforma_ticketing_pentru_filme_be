package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.Venue;
import com.example.platforma_ticketing_be.repository.VenueRepository;
import com.example.platforma_ticketing_be.repository.VenueSpecificationImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final ModelMapper modelMapper;
    private final VenueSpecificationImpl venueSpecification;

    public VenueService(VenueRepository venueRepository, ModelMapper modelMapper, VenueSpecificationImpl venueSpecification) {
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
        this.venueSpecification = venueSpecification;
    }

    private VenuePageResponseDto getVenuePageResponse(Page<Venue> pageOfVenues){
        List<Venue> venues = pageOfVenues.getContent();
        List<VenueDto> dtos = venues.stream()
                .map(venue -> this.modelMapper.map(venue, VenueDto.class))
                .collect(Collectors.toList());
        return new VenuePageResponseDto(dtos, pageOfVenues.getNumber(),
                (int) pageOfVenues.getTotalElements(), pageOfVenues.getTotalPages());
    }

    public VenuePageResponseDto findAllByPaging(int page, int size) {
        Pageable pagingSort = PageRequest.of(page, size);
        Page<Venue> pageOfVenues = this.venueRepository.findAll(pagingSort);
        return getVenuePageResponse(pageOfVenues);
    }

    public VenuePageResponseDto findAllByPagingAndFilter(VenuePageDto dto) {
        Pageable pagingSort = PageRequest.of(dto.getPage(), dto.getSize());
        Specification<Venue> specification = this.venueSpecification.getVenues(dto.getDto());
        Page<Venue> pageOfVenues = this.venueRepository.findAll(specification, pagingSort);
        return getVenuePageResponse(pageOfVenues);
    }

    public Venue create(VenueDto venueDto){
        Venue venue = this.modelMapper.map(venueDto, Venue.class);
        venueRepository.save(venue);
        return venue;
    }

    public void delete(Long id){
        Optional<Venue> venue = venueRepository.findById(id);
        if(venue.isEmpty()){
            throw new EntityNotFoundException(Venue.class.getSimpleName() + " with id: " + id);
        }
        venueRepository.deleteById(id);
    }

    public Set<VenueDto> getAllVenueNumbersOfGivenTheatre(Long theatreId){
        return this.venueRepository.getAllVenueNumbersOfGivenTheatre(theatreId).stream()
                .map(venue -> this.modelMapper.map(venue, VenueDto.class))
                .collect(Collectors.toSet());
    }

    public VenueDto getVenueById(Long id){
        if(this.venueRepository.findById(id).isPresent()){
            return this.modelMapper.map(this.venueRepository.findById(id).get(), VenueDto.class);
        }
        return null;
    }
}
