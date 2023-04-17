package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.MovieDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.service.MovieService;
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

    @GetMapping
    public List<MovieDto> getAllMoview(){
        return this.movieService.getAllMovies();
    }

    @PutMapping()
    public Movie create(@RequestParam("photo") MultipartFile file, @RequestBody MovieDto movieDto) throws IOException {
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
