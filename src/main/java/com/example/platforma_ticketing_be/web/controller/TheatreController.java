package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.TheatreDto;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.service.TheatreService;
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

    @PutMapping()
    public Theatre create(@RequestParam("photo") MultipartFile file, @RequestBody TheatreDto theatreDto) throws IOException {
        return this.theatreService.create(file, theatreDto);
    }

    /*@PostMapping()
    public void update(@RequestBody UserAccount userDTO){
        this.userService.update(userDTO);
    }*/

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        this.theatreService.delete(id);
        return ResponseEntity.ok().build();
    }
}
