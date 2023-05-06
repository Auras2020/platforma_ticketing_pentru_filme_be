package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.service.TheatreService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping
    public List<TheatreDto> getAllTheatre(){
        return this.theatreService.getAllTheatre();
    }

    @PostMapping("/page/filter")
    public TheatrePageResponseDto getAllTheatresBySpecsPage(
            @RequestBody TheatrePageDto dto) {
        return this.theatreService.findAllByPagingAndFilter(dto);
    }

    @GetMapping("/page")
    public TheatrePageResponseDto getAllTheatresPage(
            @RequestParam int page,
            @RequestParam int size) {
        return this.theatreService.findAllByPaging(page, size);
    }

    @GetMapping("/{id}")
    public TheatreDto getTheatreById(@PathVariable("id") Long id){
        return this.theatreService.getTheatreById(id);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Theatre create(@RequestPart("photo") MultipartFile file, @RequestPart("theatre") TheatreDto theatreDto) throws IOException {
        return this.theatreService.create(file, theatreDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        this.theatreService.delete(id);
        return ResponseEntity.ok().build();
    }
}
