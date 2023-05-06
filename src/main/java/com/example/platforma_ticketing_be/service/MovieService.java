package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.repository.MovieRepository;
import com.example.platforma_ticketing_be.repository.MovieSpecificationImpl;
import com.example.platforma_ticketing_be.repository.ShowTimingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final MovieSpecificationImpl movieSpecification;
    private final ShowTimingRepository showTimingRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public MovieService(MovieRepository movieRepository, ModelMapper modelMapper, MovieSpecificationImpl movieSpecification, ShowTimingRepository showTimingRepository) {
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
        this.movieSpecification = movieSpecification;
        this.showTimingRepository = showTimingRepository;
    }

    public List<MovieDto> getAllMovies(){
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(movie -> this.modelMapper.map(movie, MovieDto.class)).collect(Collectors.toList());
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

    public Movie create(MultipartFile posterFile, String trailerFile, MovieDto movieDto) throws IOException {
        Movie movie;
        if(movieDto.getId() != null){
            if(this.movieRepository.findById(movieDto.getId()).isPresent()){
                movie = this.movieRepository.findById(movieDto.getId()).get();
                if(this.checkIfUploadedFileIsOfImageType(posterFile)){
                    movieDto.setPoster(posterFile.getBytes());
                    movieDto.setPosterName(posterFile.getOriginalFilename());
                } else if(posterFile.isEmpty()){
                    movieDto.setPoster(movie.getPoster());
                    movieDto.setPosterName(movie.getPosterName());
                }
            }
        } else {
            if(this.checkIfUploadedFileIsOfImageType(posterFile)){
                movieDto.setPoster(posterFile.getBytes());
                movieDto.setPosterName(posterFile.getOriginalFilename());
            }
        }
        if(trailerFile != null){
            movieDto.setTrailerName(trailerFile);
        }
        movie = this.modelMapper.map(movieDto, Movie.class);
        movieRepository.save(movie);
        return movie;
    }

    public void delete(Long id){
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isEmpty()) {
            throw new EntityNotFoundException(Movie.class.getSimpleName() + " with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private boolean checkIfUploadedFileIsOfImageType(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).contains("image");
    }

    private boolean checkIfUploadedFileIsOfVideoType(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).contains("video");
    }

    public MovieDto getMovieById(Long id){
        if(this.movieRepository.findById(id).isPresent()){
            return this.modelMapper.map(this.movieRepository.findById(id).get(), MovieDto.class);
        }
        return null;
    }

    public List<Movie> filterMovies(Set<Movie> movies, Specification<Movie> specification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            Predicate moviePredicate = builder.and(predicate, builder.equal(root, movie));
            query.where(moviePredicate);

            List<Movie> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }

    public List<MoviesTimesDto> getAllMoviesFromATheatreAtAGivenDay(MovieFilterDto movieFilterDto, Long theatreId, Date day){
        List<MoviesTimesDto> moviesTimesDtos = new ArrayList<>();
        Specification<Movie> specification = this.movieSpecification.getMovies(movieFilterDto);
        Set<Movie> movies = this.showTimingRepository.getAllMoviesFromATheatre(theatreId).stream()
                .filter(showTiming -> showTiming.getDay().getDate() == day.getDate() && showTiming.getDay().getMonth() == day.getMonth())
                .map(ShowTiming::getMovie)
                .collect(Collectors.toSet());

        List<Movie> filteredMovies = filterMovies(movies, specification);

        for(Movie movie: filteredMovies){
            List<String> times = this.showTimingRepository.getAllTimesOfAMovieInADayFromATheatre(theatreId, movie.getId())
                    .stream()
                    .filter(showTiming1 -> showTiming1.getDay().getDate() == day.getDate() && showTiming1.getDay().getMonth() == day.getMonth())
                    .map(ShowTiming::getTime)
                    .toList();
            moviesTimesDtos.add(new MoviesTimesDto(this.modelMapper.map(movie, MovieDto.class), times));
        }
        return moviesTimesDtos;
    }
}
