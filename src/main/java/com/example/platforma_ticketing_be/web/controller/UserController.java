package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.UserCreateDTO;
import com.example.platforma_ticketing_be.dtos.UserPageDto;
import com.example.platforma_ticketing_be.dtos.UserPageResponseDto;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

   /* @GetMapping("/current")
    public UserAccount getCurrentUser(){
        return this.userService.getCurrentUser();
    }*/

    @GetMapping
    public List<UserCreateDTO> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @PostMapping("/page/filter")
    public UserPageResponseDto getAllUsersBySpecsPage(
            @RequestBody UserPageDto dto) {
        return this.userService.findAllByPagingAndFilter(dto);
    }

    @GetMapping("/page")
    public UserPageResponseDto getAllUsersPage(
            @RequestParam int page,
            @RequestParam int size) {
        return this.userService.findAllByPaging(page, size);
    }

    @PutMapping()
    public UserAccount create(@RequestBody UserCreateDTO userCreateDTO){
        return this.userService.create(userCreateDTO);
    }

    @PostMapping()
    public void update(@RequestBody UserAccount userDTO){
        this.userService.update(userDTO);
    }

    @DeleteMapping(value = "/{email}")
    public void delete(@PathVariable("email") String email){
        this.userService.delete(email);
    }
}
