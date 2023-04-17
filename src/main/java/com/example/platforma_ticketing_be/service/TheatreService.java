package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.TheatreDto;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.repository.TheatreRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TheatreService {

    private static final Logger LOG = LoggerFactory.getLogger(TheatreService.class);
    private final TheatreRepository theatreRepository;
    private final ModelMapper modelMapper;


    public TheatreService(TheatreRepository theatreRepository, ModelMapper modelMapper) {
        this.theatreRepository = theatreRepository;
        this.modelMapper = modelMapper;
    }

    public List<TheatreDto> getAllTheatre(){
        List<Theatre> theatres = theatreRepository.findAll();
        return theatres.stream().map(theatre -> this.modelMapper.map(theatre, TheatreDto.class)).collect(Collectors.toList());
    }

    public Theatre create(MultipartFile file, TheatreDto theatreDto) throws IOException {
        changePhoto(file, theatreDto);
        Theatre theatre = this.modelMapper.map(theatreDto, Theatre.class);
        theatreRepository.save(theatre);
        return theatre;
    }

    /*public void update(Movie movie){
        this.movieRepository.save(movie);
    }*/

    public void delete(Long id){
        Optional<Theatre> theatreOptional = theatreRepository.findById(id);
        if (theatreOptional.isEmpty()) {
            throw new EntityNotFoundException(Theatre.class.getSimpleName() + " with id: " + id);
        }
        theatreRepository.deleteById(id);
    }

    private void checkImage(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).contains("image")) {
            LOG.info("{} is not of image type!!!", file.getName());
        }
    }

    private void changePhoto(MultipartFile file, TheatreDto theatreDto) throws IOException {
        this.checkImage(file);
        theatreDto.setPoster(file.getBytes());
    }
}
