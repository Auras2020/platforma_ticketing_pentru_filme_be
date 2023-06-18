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

    @PostMapping("/active/page/filter")
    public UserPResponseDto getAllFilteredActiveAccounts(@RequestBody AdminUsersDto adminUsersDto) {
        return this.userService.getAllFilteredActiveAccounts(adminUsersDto.getUserFilterDto(), adminUsersDto.getDto());
    }

    @PostMapping("/active/page")
    public UserPResponseDto getAllActiveAccounts(@RequestBody UserPDto dto) {
        return this.userService.getAllActiveAccounts(dto);
    }

    @PostMapping("/pending/page/filter")
    public UserPResponseDto getAllFilteredPendingAccounts(@RequestBody AdminUsersDto adminUsersDto) {
        return this.userService.getAllFilteredPendingAccounts(adminUsersDto.getUserFilterDto(), adminUsersDto.getDto());
    }

    @PostMapping("/pending/page")
    public UserPResponseDto getAllPendingAccounts(@RequestBody UserPDto dto) {
        return this.userService.getAllPendingAccounts(dto);
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

    @GetMapping("/approve-request/{email}")
    public void approveRequest(@PathVariable("email") String email){
        this.userService.approveRequest(email);
    }

    @GetMapping("/check-for-pending-registration")
    public int checkIfThereArePendingRequests(){
        return this.userService.checkIfThereArePendingRequests();
    }
}
