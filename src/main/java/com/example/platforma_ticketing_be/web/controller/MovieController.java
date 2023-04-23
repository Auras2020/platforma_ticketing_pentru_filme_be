package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.service.MovieService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/page")
    public MoviePageResponseDto getAllMoviesPage(
            @RequestParam int page,
            @RequestParam int size) {
        return this.movieService.findAllByPaging(page, size);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Movie create(@RequestPart("photo") MultipartFile file, @RequestPart("movie") MovieDto movieDto) throws IOException {
        return this.movieService.create(file, movieDto);
    }

   /* @PostMapping()
    public void update(@RequestBody Movie movie){
        this.movieService.
    }*/

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        this.movieService.delete(id);
        return ResponseEntity.ok().build();
    }
}
