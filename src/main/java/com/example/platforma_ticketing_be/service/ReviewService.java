package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.Review;
import com.example.platforma_ticketing_be.repository.MovieRepository;
import com.example.platforma_ticketing_be.repository.MovieSpecificationImpl;
import com.example.platforma_ticketing_be.repository.ReviewRepository;
import com.example.platforma_ticketing_be.repository.ReviewSpecificationImpl;
import org.modelmapper.ModelMapper;
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
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewSpecificationImpl reviewSpecification;
    private final ModelMapper modelMapper;
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final MovieSpecificationImpl movieSpecification;
    @PersistenceContext
    private EntityManager entityManager;

    public ReviewService(ReviewRepository reviewRepository, ReviewSpecificationImpl reviewSpecification, ModelMapper modelMapper, MovieService movieService, MovieRepository movieRepository, MovieSpecificationImpl movieSpecification) {
        this.reviewRepository = reviewRepository;
        this.reviewSpecification = reviewSpecification;
        this.modelMapper = modelMapper;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.movieSpecification = movieSpecification;
    }

    public void createReview(ReviewDto reviewDto){
        if(reviewDto.getMovie() != null){
            this.reviewRepository.save(this.modelMapper.map(reviewDto, Review.class));
        }
    }

    public void deleteById(Long id){
        this.reviewRepository.deleteById(id);
    }

    public Set<Long> filterReviews(List<Review> reviews, ReviewFilterDto reviewFilterDto) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> query = builder.createQuery(Review.class);
        Root<Review> root = query.from(Review.class);
        Specification<Review> specification = this.reviewSpecification.getReviews(reviewFilterDto);

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        Set<Long> filteredReviews = new HashSet<>();
        for (Review review: reviews) {
            Predicate reviewPredicate = builder.and(predicate, builder.equal(root, review));
            query.where(reviewPredicate);
            List<Review> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredReviews.add(review.getId());
            }
        }

        return filteredReviews;
    }

    public List<MovieReviewDto> getAllReviews(ReviewFilterDto reviewFilterDto){
        MovieFilterDto movieFilterDto = new MovieFilterDto(reviewFilterDto.getMovieName(), reviewFilterDto.getRecommendedAge(),
                reviewFilterDto.getGenre(), reviewFilterDto.getSearchString());
        Specification<Movie> specification = this.movieSpecification.getMovies(movieFilterDto);
        List<Movie> filteredMovies = this.movieService.filterMovies(new HashSet<>(this.movieRepository.findAll()), specification);
        Set<Long> filteredReviewsIds = filterReviews(this.reviewRepository.findAll(), reviewFilterDto);
        List<Long> filteredMoviesIds = filteredMovies.stream()
                .map(Movie::getId)
                .toList();
        boolean isReviewFilterEmpty = Objects.equals(reviewFilterDto.getReviewName(), "")
                && Objects.equals(reviewFilterDto.getReviewOpinion(), "");
        List<Object[]> objects = this.reviewRepository.findFilteredReviews(filteredMoviesIds, filteredReviewsIds, isReviewFilterEmpty);
        List<MovieReviewDto> movieReviewDtoList = new ArrayList<>();
        Movie prevMovie = null;
        List<ReviewDto> reviews = new ArrayList<>();

        for(Object[] object: objects){
            Movie movie = (Movie) object[0];
            Review review = (Review) object[1];

            if(movie != prevMovie && prevMovie != null){
                MovieReviewDto movieReviewDto = new MovieReviewDto(this.modelMapper.map(prevMovie, MovieDto.class), reviews);
                movieReviewDtoList.add(movieReviewDto);
                reviews = new ArrayList<>();
            }
            if(review != null){
                reviews.add(this.modelMapper.map(review, ReviewDto.class));
            }
            prevMovie = movie;
        }
        if(prevMovie != null){
            MovieReviewDto movieReviewDto = new MovieReviewDto(this.modelMapper.map(prevMovie, MovieDto.class), reviews);
            movieReviewDtoList.add(movieReviewDto);
        }
        return movieReviewDtoList;
    }

    public List<MovieReviewDto> getAllReviewsFilteredByCurrentUser(ReviewFilterDto reviewFilterDto, Long userId){
        List<MovieReviewDto> movieReviewDtoList = getAllReviews(reviewFilterDto);
        List<MovieReviewDto> movieReviewDtoList1 = new ArrayList<>();
        for(MovieReviewDto movieReviewDto: movieReviewDtoList){
            List<ReviewDto> reviewDtos = new ArrayList<>();
            for(ReviewDto review: movieReviewDto.getReviews()){
                if(Objects.equals(review.getUser().getId(), userId)){
                    reviewDtos.add(review);
                }
            }
            if(reviewDtos.size() > 0){
                movieReviewDtoList1.add(new MovieReviewDto(movieReviewDto.getMovie(), reviewDtos));
            }
        }
        return movieReviewDtoList1;
    }

    public List<ReviewDto> getAllExistingReviewsByMovieId(Long id){
        return this.reviewRepository.getAllExistingReviewsByMovieId(id).stream()
                .map(review -> this.modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }
}
