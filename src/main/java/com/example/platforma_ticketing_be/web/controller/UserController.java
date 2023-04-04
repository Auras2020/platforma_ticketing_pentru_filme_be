package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.UserCreateDTO;
import com.example.platforma_ticketing_be.dtos.UserCreateResponseDTO;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping()
    public UserAccount create(@RequestBody UserCreateDTO userCreateDTO){
        return this.userService.create(userCreateDTO);
    }

    @PostMapping()
    public void update(@RequestBody UserAccount userDTO){
        this.userService.update(userDTO);
    }
}
