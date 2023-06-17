package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping(value = "/{email}")
    public void delete(@PathVariable("email") String email){
        this.userService.delete(email);
    }

    @GetMapping("/dashboard")
    public DashboardDto getCurrentInfo(){
        return this.userService.getCurrentInfo();
    }

    @GetMapping("/theatre-manager/dashboard/{id}")
    public TheatreManagerDashboardDto getCurrentInfoTheatreManager(@PathVariable("id") Long theatreId){
        return this.userService.getCurrentInfoTheatreManager(theatreId);
    }

    @GetMapping("/{email}")
    public UserAccount getUserByEmail(@PathVariable("email") String email){
        return this.userService.findByEmail(email);
    }
}
