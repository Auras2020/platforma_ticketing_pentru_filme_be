package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.*;
import com.example.platforma_ticketing_be.repository.ShowTimingRepository;
import com.example.platforma_ticketing_be.repository.ShowTimingSpecificationImpl;
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
import java.util.stream.Collectors;

@Service
public class ShowTimingService {

    private final ShowTimingRepository showTimingRepository;
    private final ModelMapper modelMapper;
    private final ShowTimingSpecificationImpl showTimingSpecification;

    public ShowTimingService(ShowTimingRepository showTimingRepository, ModelMapper modelMapper, ShowTimingSpecificationImpl showTimingSpecification) {
        this.showTimingRepository = showTimingRepository;
        this.modelMapper = modelMapper;
        this.showTimingSpecification = showTimingSpecification;
    }

    private ShowTimingPageResponseDto getShowTimingPageResponse(Page<ShowTiming> pageOfShowTimings){
        List<ShowTiming> showTimings = pageOfShowTimings.getContent();
        List<ShowTimingDto> dtos = showTimings.stream()
                .map(showTiming -> this.modelMapper.map(showTiming, ShowTimingDto.class))
                .collect(Collectors.toList());
        return new ShowTimingPageResponseDto(dtos, pageOfShowTimings.getNumber(),
                (int) pageOfShowTimings.getTotalElements(), pageOfShowTimings.getTotalPages());
    }

    public ShowTimingPageResponseDto findAllByPaging(int page, int size) {
        Pageable pagingSort = PageRequest.of(page, size);
        Page<ShowTiming> pageOfShowTimings = this.showTimingRepository.findAll(pagingSort);
        return getShowTimingPageResponse(pageOfShowTimings);
    }

    public ShowTimingPageResponseDto findAllByPagingAndFilter(ShowTimingPageDto dto) {
        Pageable pagingSort = PageRequest.of(dto.getPage(), dto.getSize());
        Specification<ShowTiming> specification = this.showTimingSpecification.getShowTimings(dto.getDto());
        Page<ShowTiming> pageOfShowTimings = this.showTimingRepository.findAll(specification, pagingSort);
        return getShowTimingPageResponse(pageOfShowTimings);
    }

    public ShowTiming create(ShowTimingDto showTimingDto){
        ShowTiming showTiming = this.modelMapper.map(showTimingDto, ShowTiming.class);
        showTimingRepository.save(showTiming);
        return showTiming;
    }

    public void delete(Long id){
        Optional<ShowTiming> showTiming = showTimingRepository.findById(id);
        if(showTiming.isEmpty()){
            throw new EntityNotFoundException(ShowTiming.class.getSimpleName() + " with id: " + id);
        }
        showTimingRepository.deleteById(id);
    }

    public ShowTimingDto findShowTimingByShowTimingDetails(Long theatreId, Long movieId, Date day, String time){
        List<ShowTiming> showTimings = this.showTimingRepository.findShowTimingByShowTimingDetails(theatreId, movieId, time).stream()
                .filter(showTiming -> showTiming.getDay().getDate() == day.getDate() && showTiming.getDay().getMonth() == day.getMonth())
                .toList();
        return showTimings.isEmpty() ? null : this.modelMapper.map(showTimings.get(0), ShowTimingDto.class);
    }

    public ShowTimingDto getShowTimingById(Long id){
        if(this.showTimingRepository.findById(id).isPresent()){
            return this.modelMapper.map(this.showTimingRepository.findById(id).get(), ShowTimingDto.class);
        }
        return null;
    }
}
