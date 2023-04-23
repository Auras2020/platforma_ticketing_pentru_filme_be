package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.repository.MovieRepository;
import com.example.platforma_ticketing_be.repository.MovieSpecificationImpl;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final MovieSpecificationImpl movieSpecification;

    public MovieService(MovieRepository movieRepository, ModelMapper modelMapper, MovieSpecificationImpl movieSpecification) {
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
        this.movieSpecification = movieSpecification;
    }

    private MoviePageResponseDto getMoviePageResponse(Page<Movie> pageOfMovies){
        List<Movie> movies = pageOfMovies.getContent();
        List<MovieDto> dtos = movies.stream()
                .map(movie -> this.modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());
        return new MoviePageResponseDto(dtos, pageOfMovies.getNumber(),
                (int) pageOfMovies.getTotalElements(), pageOfMovies.getTotalPages());
    }

    public MoviePageResponseDto findAllByPaging(int page, int size) {
        Pageable pagingSort = PageRequest.of(page, size);
        Page<Movie> pageOfMovies = this.movieRepository.findAll(pagingSort);
        return getMoviePageResponse(pageOfMovies);
    }

    public MoviePageResponseDto findAllByPagingAndFilter(MoviePageDto dto) {
        Pageable pagingSort = PageRequest.of(dto.getPage(), dto.getSize());
        Specification<Movie> specification = this.movieSpecification.getMovies(dto.getDto());
        Page<Movie> pageOfMovies = this.movieRepository.findAll(specification, pagingSort);
        return getMoviePageResponse(pageOfMovies);
    }

    public Movie create(MultipartFile file, MovieDto movieDto) throws IOException {
        changePhoto(file, movieDto);
        Movie movie = this.modelMapper.map(movieDto, Movie.class);
        movie.setPosterName(file.getOriginalFilename());
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
