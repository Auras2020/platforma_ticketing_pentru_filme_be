package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.UserCreateDTO;
import com.example.platforma_ticketing_be.dtos.UserCreateResponseDTO;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.entities.UserRole;
import com.example.platforma_ticketing_be.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserAccount create(UserCreateDTO userCreateDTO){
        UserAccount userAccount = this.modelMapper.map(userCreateDTO, UserAccount.class);
        userAccount.setRole(String.valueOf(UserRole.client));
        userRepository.save(userAccount);
        return userAccount;
     /*   long userId = userRepository.save(userAccount).getId();

        return UserCreateResponseDTO.builder()
                .isOk(true)
                .id(userId)
                .message("User added successfully")
                .build();*/
    }

    public void update(UserAccount dto){
        Optional<UserAccount> userAccountOptional = userRepository.findById(dto.getId());
        UserAccount userAccount = this.modelMapper.map(dto, UserAccount.class);
        userAccount.setId(dto.getId());

        if(userAccountOptional.get().getPassword()!=null){
            userAccount.setPassword(userAccountOptional.get().getPassword());
        }
        else {
            userAccount.setPassword("");
        }

        this.userRepository.save(userAccount);
    }

    public UserAccount getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("username: " + username);
        return userRepository.findByEmail(username);
    }
}
