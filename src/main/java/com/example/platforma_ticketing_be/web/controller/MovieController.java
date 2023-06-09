package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.service.MovieService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/page/filter")
    public MoviePageResponseDto getAllMoviesBySpecsPage(
            @RequestBody MoviePageDto dto) {
        return this.movieService.findAllByPagingAndFilter(dto);
    }

    @GetMapping
    public List<MovieDto> getAllMovies(){
        return this.movieService.getAllMovies();
    }

    @GetMapping("/page")
    public MoviePageResponseDto getAllMoviesPage(
            @RequestParam int page,
            @RequestParam int size) {
        return this.movieService.findAllByPaging(page, size);
    }

    @GetMapping("/videos/{fileName:.+}")
    public ResponseEntity<InputStreamResource> getVideo(@PathVariable String fileName) throws FileNotFoundException {
        File videoFile = new File("D:/trailers/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(videoFile));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoFile.getName() + "\"")
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }

    @GetMapping("/theatre/{id}")
    public Set<MovieDto> getAllMoviesFromATheatre(@PathVariable("id") Long theatreId){
        return this.movieService.getAllMoviesFromATheatre(theatreId);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void create(@RequestPart("photo") MultipartFile posterFile, @RequestPart("trailer") String trailerFile, @RequestPart("movie") MovieDto movieDto) throws IOException {
        this.movieService.create(posterFile, trailerFile, movieDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        this.movieService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable("id") Long id){
        return this.movieService.getMovieById(id);
    }

    @PostMapping("/theatre-and-day")
    public Set<MovieDto> getAllMoviesFromATheatreAtAGivenDay(@RequestBody TheatreIdDayDto theatreIdDayDto){
        return this.movieService.getAllMoviesFromATheatreAtAGivenDay(theatreIdDayDto.getTheatreId(), theatreIdDayDto.getDay());
    }

    @PostMapping("/movies-and-times")
    public List<MoviesTimesDto> getAllMoviesFromATheatreAtAGivenDay(@RequestBody TheatreDayDto theatreDayDto){
        return this.movieService.getAllMoviesFromATheatreAtAGivenDay(theatreDayDto.getMovieFilter(), theatreDayDto.getTheatreId(), theatreDayDto.getDay());
    }

    @PostMapping("/show-timings")
    public Set<String> getAllTimesByShowTiming(@RequestBody TheatreMovieDayDto theatreMovieDayDto){
        return this.movieService.getAllTimesByShowTiming(theatreMovieDayDto.getTheatreId(), theatreMovieDayDto.getMovieId(), theatreMovieDayDto.getDay());
    }

    @PostMapping("/current-running")
    public List<MovieDto> getAllMoviesCurrentlyRunning(@RequestBody MovieFilterDto movieFilterDto){
        return this.movieService.getAllMoviesCurrentlyRunning(movieFilterDto);
    }

    @PostMapping("/soon-running")
    public List<MovieDto> getAllMoviesRunningSoon(@RequestBody MovieFilterDto movieFilterDto){
        return this.movieService.getAllMoviesRunningSoon(movieFilterDto);
    }

    @PostMapping("/age")
    public List<MovieDto> getRecomendedMovies(@RequestBody MovieFilterAgeDto movieFilterAgeDto){
        return this.movieService.getRecomendedMovies(movieFilterAgeDto.getMovieFilter(), movieFilterAgeDto.getAge(), movieFilterAgeDto.getCreatedDate());
    }

    @GetMapping("/genres/{id}")
    public List<GenreDto> findGenresOfAMovie(@PathVariable Long id){
        return this.movieService.findGenresOfAMovie(id);
    }

    @GetMapping("genres")
    public List<GenreDto> getAllGenres(){
        return this.movieService.getAllGenres();
    }
}
