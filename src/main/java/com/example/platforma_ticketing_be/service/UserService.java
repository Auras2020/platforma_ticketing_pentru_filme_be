package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.repository.*;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private final EmailServiceImpl emailService;
    private final ShowTimingRepository showTimingRepository;
    private final VenueRepository venueRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, UserSpecificationImpl userSpecification, TheatreRepository theatreRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, MovieRepository movieRepository, EmailServiceImpl emailService, ShowTimingRepository showTimingRepository, VenueRepository venueRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userSpecification = userSpecification;
        this.theatreRepository = theatreRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.emailService = emailService;
        this.showTimingRepository = showTimingRepository;
        this.venueRepository = venueRepository;
    }

    public UserAccount create(UserCreateDTO userCreateDTO){
        UserAccount userAccount = this.modelMapper.map(userCreateDTO, UserAccount.class);
        userAccount.setCreatedDate(new Date());
        userRepository.save(userAccount);
        String subject = "";
        String body = "";
        if(userAccount.getRole().equals("CLIENT")){
            subject = "Account Registration Confirmation";
            body = "Your account was successfully registered!";
        } else {
            subject = "Account Registration Confirmation";
            body = "Your account was successfully registered! " +
                    "You will receive a confirmation mail when your request to login into application will be approved.";
        }

        this.emailService.sendEmail(subject, body, userCreateDTO.getEmail());
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

    public TheatreManagerDashboardDto getCurrentInfoTheatreManager(Long theatreId){
        int movies = this.showTimingRepository.countMoviesFromATheatre(theatreId);
        int venues = this.venueRepository.getVenuesNumberFromTheatre(theatreId);
        int tickets = this.orderRepository.getNumberOfTicketsSoldFromATheatre(theatreId);
        int products = this.orderRepository.getNumberOfProductsSoldFromATheatre(theatreId) == null
                ? 0
                : this.orderRepository.getNumberOfProductsSoldFromATheatre(theatreId);
        return new TheatreManagerDashboardDto(movies, venues, tickets, products);
    }

    public UserAccount findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
}
