package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.entities.UserRole;
import com.example.platforma_ticketing_be.repository.UserRepository;
import com.example.platforma_ticketing_be.repository.UserSpecificationImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserSpecificationImpl userSpecification;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, UserSpecificationImpl userSpecification) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userSpecification = userSpecification;
    }

    public UserAccount create(UserCreateDTO userCreateDTO){
        UserAccount userAccount = this.modelMapper.map(userCreateDTO, UserAccount.class);
        userAccount.setRole(String.valueOf(UserRole.CLIENT));
        userRepository.save(userAccount);
        return userAccount;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

                // Cast the principal to your user model class

                System.out.println("eee " + principal);
                return this.userRepository.findByEmail(principal + "");
                // ...

        }
        return null;
    }

    private UserPageResponseDto getUserPageResponse(Page<UserAccount> pageOfUsers){
        List<UserAccount> users = pageOfUsers.getContent();
        List<UserCreateDTO> dtos = users.stream()
                .map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class))
                .collect(Collectors.toList());
        return new UserPageResponseDto(dtos, pageOfUsers.getNumber(),
                (int) pageOfUsers.getTotalElements(), pageOfUsers.getTotalPages());
    }

    public UserPageResponseDto findAllByPaging(int page, int size) {
        Pageable pagingSort = PageRequest.of(page, size);
        Page<UserAccount> pageOfUsers = this.userRepository.findAll(pagingSort);
        return getUserPageResponse(pageOfUsers);
    }

    public UserPageResponseDto findAllByPagingAndFilter(UserPageDto dto) {
        /*System.out.println("usr: " + getCurrentUser());*/
        Pageable pagingSort = PageRequest.of(dto.getPage(), dto.getSize());
        Specification<UserAccount> specification = this.userSpecification.getUsers(dto.getDto());
        Page<UserAccount> pageOfUsers = this.userRepository.findAll(specification, pagingSort);
        return getUserPageResponse(pageOfUsers);
    }

    public List<UserCreateDTO> getAllUsers(){
        List<UserAccount> userAccounts = userRepository.findAll();
        return userAccounts.stream().map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class)).collect(Collectors.toList());
    }

    public void delete(String email){
       UserAccount user = userRepository.findByEmail(email);
       userRepository.delete(user);
    }
}
