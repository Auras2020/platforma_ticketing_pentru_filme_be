package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.MovieDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.repository.MovieRepository;
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
public class MovieService {
    private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    public MovieService(MovieRepository movieRepository, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
    }

    public List<MovieDto> getAllMovies(){
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(movie -> this.modelMapper.map(movie, MovieDto.class)).collect(Collectors.toList());
    }

    public Movie create(MultipartFile file, MovieDto movieDto) throws IOException {
        changePhoto(file, movieDto);
        Movie movie = this.modelMapper.map(movieDto, Movie.class);
        movieRepository.save(movie);
        return movie;
    }

    /*public void update(Movie movie){
        this.movieRepository.save(movie);
    }*/

    public void delete(Long id){
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isEmpty()) {
            throw new EntityNotFoundException(Movie.class.getSimpleName() + " with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private void checkImage(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).contains("image")) {
            LOG.info("{} is not of image type!!!", file.getName());
        }
    }

    private void changePhoto(MultipartFile file, MovieDto movieDto) throws IOException {
        this.checkImage(file);
        movieDto.setPoster(file.getBytes());
    }
}
