package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.repository.*;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
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
    @PersistenceContext
    private EntityManager entityManager;

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
        if(!userCreateDTO.getRole().equals("CLIENT")){
            userAccount.setPending(true);
        }
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

    public void approveRequest(String email){
        UserAccount userAccount = this.userRepository.findByEmail(email);
        userAccount.setPending(false);
        this.userRepository.save(userAccount);

        String subject = "Approved Registration";
        String body = "Your registration was approved by admin!";
        this.emailService.sendEmail(subject, body, email);
    }

    public Set<Long> filterUsers(List<UserAccount> userAccounts, Specification<UserAccount> specification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserAccount> query = builder.createQuery(UserAccount.class);
        Root<UserAccount> root = query.from(UserAccount.class);

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        Set<Long> filteredUsers = new HashSet<>();
        for (UserAccount userAccount: userAccounts) {
            Predicate userPredicate = builder.and(predicate, builder.equal(root, userAccount));
            query.where(userPredicate);

            List<UserAccount> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredUsers.add(userAccount.getId());
            }
        }

        return filteredUsers;
    }

    public UserPResponseDto getAllActiveAccounts(UserPDto dto){
        List<UserAccount> userAccounts = this.userRepository.getAllActiveAccounts(PageRequest.of(dto.getPage(), dto.getSize()));
        List<UserCreateDTO> userCreateDTOS = userAccounts.stream()
                .map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class))
                .toList();

        int totalUsers = 0;
        if(this.userRepository.findAll().size() > 0){
            totalUsers = this.userRepository.getAllActiveAccounts(PageRequest.of(0, this.userRepository.findAll().size())).size();
        }

        return new UserPResponseDto(userCreateDTOS, totalUsers);
    }

    public UserPResponseDto getAllFilteredActiveAccounts(UserFilterDto userFilterDto, UserPDto dto){
        Specification<UserAccount> specification = this.userSpecification.getUsers(userFilterDto);
        Set<Long> filteredUsers = filterUsers(this.userRepository.findAll(), specification);
        int totalUsers = 0;
        List<UserCreateDTO> userCreateDTOS = new ArrayList<>();
        if(this.userRepository.findAll().size() > 0){
            Set<UserAccount> userAccounts = new HashSet<>(this.userRepository.getAllFilteredActiveAccounts(PageRequest.of(dto.getPage(), dto.getSize()), filteredUsers));
            userCreateDTOS = userAccounts.stream()
                    .map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class))
                    .toList();
            totalUsers = this.userRepository.getAllFilteredActiveAccounts(PageRequest.of(0, this.userRepository.findAll().size()), filteredUsers).size();
        }
        return new UserPResponseDto(userCreateDTOS, totalUsers);
    }

    public UserPResponseDto getAllPendingAccounts(UserPDto dto){
        List<UserAccount> userAccounts = this.userRepository.getAllPendingAccounts(PageRequest.of(dto.getPage(), dto.getSize()));
        List<UserCreateDTO> userCreateDTOS = userAccounts.stream()
                .map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class))
                .toList();

        int totalUsers = 0;
        if(this.userRepository.findAll().size() > 0){
            totalUsers = this.userRepository.getAllPendingAccounts(PageRequest.of(0, this.userRepository.findAll().size())).size();
        }

        return new UserPResponseDto(userCreateDTOS, totalUsers);
    }

    public UserPResponseDto getAllFilteredPendingAccounts(UserFilterDto userFilterDto, UserPDto dto){
        Specification<UserAccount> specification = this.userSpecification.getUsers(userFilterDto);
        Set<Long> filteredUsers = filterUsers(this.userRepository.findAll(), specification);
        int totalUsers = 0;
        List<UserCreateDTO> userCreateDTOS = new ArrayList<>();
        if(this.userRepository.findAll().size() > 0){
            Set<UserAccount> userAccounts = new HashSet<>(this.userRepository.getAllFilteredPendingAccounts(PageRequest.of(dto.getPage(), dto.getSize()), filteredUsers));
            userCreateDTOS = userAccounts.stream()
                    .map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class))
                    .toList();
            totalUsers = this.userRepository.getAllFilteredPendingAccounts(PageRequest.of(0, this.userRepository.findAll().size()), filteredUsers).size();
        }
        return new UserPResponseDto(userCreateDTOS, totalUsers);
    }

    public List<UserCreateDTO> getAllUsers(){
        List<UserAccount> userAccounts = userRepository.findAll();
        return userAccounts.stream().map(userAccount -> this.modelMapper.map(userAccount, UserCreateDTO.class)).collect(Collectors.toList());
    }

    public void delete(String email){
       UserAccount user = userRepository.findByEmail(email);

        String subject = "";
        String body = "";
        if(user.isPending()){
           subject = "Deleted Registration";
           body = "Your registration was deleted by admin!";
       } else {
           subject = "Deleted Account";
           body = "Your account was deleted by admin!";
       }
       this.emailService.sendEmail(subject, body, email);

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
        int movies = this.showTimingRepository.countMoviesFromATheatre(theatreId).size();
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

    public int checkIfThereArePendingRequests(){
        return this.userRepository.findNumberOfPendingRegistrations();
    }
}
