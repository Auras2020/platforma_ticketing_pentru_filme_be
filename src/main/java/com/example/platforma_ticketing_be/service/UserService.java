package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.entities.UserRole;
import com.example.platforma_ticketing_be.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserSpecificationImpl userSpecification;
    private final TheatreRepository theatreRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, UserSpecificationImpl userSpecification, TheatreRepository theatreRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userSpecification = userSpecification;
        this.theatreRepository = theatreRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
    }

    public UserAccount create(UserCreateDTO userCreateDTO){
        UserAccount userAccount = this.modelMapper.map(userCreateDTO, UserAccount.class);
        userAccount.setRole(String.valueOf(UserRole.CLIENT));
        userRepository.save(userAccount);
        return userAccount;
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

    public DashboardDto getCurrentInfo(){
        int theatres = this.theatreRepository.findAll().size();
        int movies = this.movieRepository.findAll().size();
        int users = this.userRepository.findAll().size();
        int tickets = this.orderRepository.getNumberOfTicketsSold();
        int products = this.orderRepository.getNumberOfProductsSold() == null ? 0 : this.orderRepository.getNumberOfProductsSold();
        int reviews = this.reviewRepository.getNumberOfReviews();
        return new DashboardDto(theatres, movies, users, tickets, products, reviews);
    }

    public UserAccount findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
}
