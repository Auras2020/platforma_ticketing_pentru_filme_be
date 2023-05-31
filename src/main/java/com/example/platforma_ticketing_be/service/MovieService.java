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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    private boolean hourAndMinuteBiggerThanCurrentDate(String time){
        if(time == null){
            return false;
        }
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        return hour > new Date().getHours() || (hour == new Date().getHours() && minute > new Date().getMinutes());
    }

    public Set<MovieDto> getAllMoviesFromATheatreAtAGivenDay(Long theatreId, Date day){
        return this.showTimingRepository.getAllMoviesFromATheatreAtAGivenDay(theatreId, day).stream()
                .map(movie -> this.modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toSet());
    }

    public Set<MovieDto> getAllMoviesFromATheatre(Long theatreId){
        return this.showTimingRepository.getAllMoviesFromATheatre(theatreId).stream()
                .map(ShowTiming::getMovie)
                .map(movie -> this.modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toSet());
    }

    public Set<String> getAllTimesByShowTiming(Long theatreId, Long movieId, Date day){
        return this.showTimingRepository.getAllTimesByShowTiming(theatreId, movieId, day);
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
                    .filter(showTiming1 -> showTiming1.getDay().getDate() == day.getDate() && showTiming1.getDay().getMonth() == day.getMonth() && showTiming1.getDay().getYear() == day.getYear())
                    .map(ShowTiming::getTime)
                    .filter(time -> day.getDay() != new Date().getDay() ||
                            (day.getDay() == new Date().getDay() && hourAndMinuteBiggerThanCurrentDate(time)))
                    .toList();
            moviesTimesDtos.add(new MoviesTimesDto(this.modelMapper.map(movie, MovieDto.class), times));
        }
        return moviesTimesDtos;
    }

    private boolean moviesCurrentlyRunning(Date startDate, Date endDate){
        LocalDate date1 = LocalDate.now();
        LocalDate startDate1 = LocalDate.of(startDate.getYear() + 1900, startDate.getMonth() + 1, startDate.getDate());
        LocalDate endDate1 = LocalDate.of(endDate.getYear() + 1900, endDate.getMonth() + 1, endDate.getDate());

        long differenceInDays1 = ChronoUnit.DAYS.between(date1, startDate1);
        long differenceInDays2 = ChronoUnit.DAYS.between(date1, endDate1);
        return differenceInDays2 >= 0 && differenceInDays1 < 7;
    }

    public List<MovieDto> getAllMoviesCurrentlyRunning(MovieFilterDto movieFilterDto){
        Specification<Movie> specification = this.movieSpecification.getMovies(movieFilterDto);
        Set<Movie> movies = this.showTimingRepository.findAll().stream()
                .filter(showTiming -> moviesCurrentlyRunning(showTiming.getStartDate(), showTiming.getEndDate()) /*showTiming.getEndDate().getDate() < new Date().getDate() + 7 && showTiming.getEndDate().getDate() >= new Date().getDate()*/)
                .map(ShowTiming::getMovie)
                .collect(Collectors.toSet());
        List<Movie> filteredMovies = filterMovies(movies, specification);
        return filteredMovies.stream()
                .map(movie -> this.modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());
    }

    private boolean moviesRunningSoon(Date date){
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
        long differenceInDays = ChronoUnit.DAYS.between(date1, date2);
        return differenceInDays >= 7;
    }

    public List<MovieDto> getAllMoviesRunningSoon(MovieFilterDto movieFilterDto){
        Specification<Movie> specification = this.movieSpecification.getMovies(movieFilterDto);
        Set<Movie> movies = this.showTimingRepository.findAll().stream()
                .filter(showTiming -> moviesRunningSoon(showTiming.getStartDate())/*showTiming.getStartDate().getDate() >= new Date().getDate() + 7*/)
                .map(ShowTiming::getMovie)
                .collect(Collectors.toSet());
        List<Movie> filteredMovies = filterMovies(movies, specification);
        return filteredMovies.stream()
                .map(movie -> this.modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());
    }

    private boolean moviesAvailable(Date date){
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
        long differenceInDays = ChronoUnit.DAYS.between(date1, date2);
        return differenceInDays >= 0;
    }

    public List<MovieDto> getRecomendedMovies(MovieFilterDto movieFilterDto, int age){
        String[] categories;
        if(age < 12){
            categories = new String[]{"AG"};
        } else if(age < 15){
            categories = new String[]{"AG", "AP12"};
        } else if(age < 18){
            categories = new String[]{"AG", "AP12", "N15"};
        } else {
            categories = new String[]{"AG", "AP12", "N15", "IM18"};
        }

        Specification<Movie> specification = this.movieSpecification.getMovies(movieFilterDto);
        //Set<Movie> movies = this.movieRepository.getAllMoviesWithACertainRecommendedAge(categories);
        Set<Movie> moviesAvailable = this.showTimingRepository.findAll().stream()
                .filter(showTiming -> moviesAvailable(showTiming.getStartDate())/*showTiming.getStartDate().getDate() >= new Date().getDate() + 7*/)
                .map(ShowTiming::getMovie)
                .filter(movie -> Arrays.asList(categories).contains(movie.getRecommendedAge()))
                .collect(Collectors.toSet());
        List<Movie> filteredMovies = filterMovies(moviesAvailable, specification);
        return filteredMovies.stream()
                .map(movie -> this.modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());
    }
}
