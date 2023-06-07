package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.repository.TheatreRepository;
import com.example.platforma_ticketing_be.repository.TheatreSpecificationImpl;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final ModelMapper modelMapper;
    private final TheatreSpecificationImpl theatreSpecification;


    public TheatreService(TheatreRepository theatreRepository, ModelMapper modelMapper, TheatreSpecificationImpl theatreSpecification) {
        this.theatreRepository = theatreRepository;
        this.modelMapper = modelMapper;
        this.theatreSpecification = theatreSpecification;
    }

    private TheatrePageResponseDto getTheatrePageResponse(Page<Theatre> pageOfTheatres){
        List<Theatre> theatres = pageOfTheatres.getContent();
        List<TheatreDto> dtos = theatres.stream()
                .map(theatre -> this.modelMapper.map(theatre, TheatreDto.class))
                .collect(Collectors.toList());
        return new TheatrePageResponseDto(dtos, pageOfTheatres.getNumber(),
                (int) pageOfTheatres.getTotalElements(), pageOfTheatres.getTotalPages());
    }

    public TheatrePageResponseDto findAllByPaging(int page, int size) {
        Pageable pagingSort = PageRequest.of(page, size);
        Page<Theatre> pageOfTheatres = this.theatreRepository.findAll(pagingSort);
        return getTheatrePageResponse(pageOfTheatres);
    }

    public TheatrePageResponseDto findAllByPagingAndFilter(TheatrePageDto dto) {
        Pageable pagingSort = PageRequest.of(dto.getPage(), dto.getSize());
        Specification<Theatre> specification = this.theatreSpecification.getTheatres(dto.getDto());
        Page<Theatre> pageOfTheatres = this.theatreRepository.findAll(specification, pagingSort);
        return getTheatrePageResponse(pageOfTheatres);
    }

    public List<TheatreDto> getAllTheatre(){
        List<Theatre> theatres = theatreRepository.getAllOrderedTheatres();
        return theatres.stream().map(theatre -> this.modelMapper.map(theatre, TheatreDto.class)).collect(Collectors.toList());
    }

    public Theatre create(MultipartFile file, TheatreDto theatreDto) throws IOException {
        Theatre theatre;
        if(theatreDto.getId() != null){
            if(this.theatreRepository.findById(theatreDto.getId()).isPresent()){
                theatre = this.theatreRepository.findById(theatreDto.getId()).get();
                if(this.checkIfUploadedFileIsOfImageType(file)){
                    theatreDto.setPoster(file.getBytes());
                    theatreDto.setPosterName(file.getOriginalFilename());
                } else if(file.isEmpty()){
                    theatreDto.setPoster(theatre.getPoster());
                    theatreDto.setPosterName(theatre.getPosterName());
                }
            }
        } else if(this.checkIfUploadedFileIsOfImageType(file)){
            theatreDto.setPoster(file.getBytes());
            theatreDto.setPosterName(file.getOriginalFilename());
        }
        theatre = this.modelMapper.map(theatreDto, Theatre.class);
        theatreRepository.save(theatre);
        return theatre;
    }

    public void delete(Long id){
        Optional<Theatre> theatreOptional = theatreRepository.findById(id);
        if (theatreOptional.isEmpty()) {
            throw new EntityNotFoundException(Theatre.class.getSimpleName() + " with id: " + id);
        }
        theatreRepository.deleteById(id);
    }

    private boolean checkIfUploadedFileIsOfImageType(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).contains("image");
    }

    public TheatreDto getTheatreById(Long id){
        if(this.theatreRepository.findById(id).isPresent()){
            return this.modelMapper.map(this.theatreRepository.findById(id).get(), TheatreDto.class);
        }
        return null;
    }

    public List<String> getAllTheatresLocations(){
        return this.theatreRepository.getAllTheatresLocations();
    }

    public List<TheatreDto> getTheatresByLocation(String location){
        return this.theatreRepository.getDistinctByLocationOrderByName(location).stream()
                .map(theatre -> this.modelMapper.map(theatre, TheatreDto.class))
                .collect(Collectors.toList());
    }
}
