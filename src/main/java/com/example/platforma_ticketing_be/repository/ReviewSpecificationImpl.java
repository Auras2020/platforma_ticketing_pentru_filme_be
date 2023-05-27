package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.MovieFilterDto;
import com.example.platforma_ticketing_be.dtos.ReviewFilterDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.MovieGenre;
import com.example.platforma_ticketing_be.entities.Review;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewSpecificationImpl {
    public Specification<Review> getReviews(ReviewFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //List<Predicate> searchPredicatesList = new ArrayList<>();

           /* if (dto.getMovieName() != null && !dto.getMovieName().isEmpty()) {
                Join<Review, Movie> reviewMovieJoin = root.join("movie", JoinType.INNER);
                predicates.add(builder.equal((reviewMovieJoin.get("name")), dto.getMovieName()));
            }
            if (dto.getRecommendedAge() != null && !dto.getRecommendedAge().isEmpty()) {
                Join<Review, Movie> reviewMovieJoin = root.join("movie", JoinType.INNER);
                predicates.add(builder.equal((reviewMovieJoin.get("recommendedAge")), dto.getRecommendedAge()));
            }
            if (dto.getGenre() != null && !dto.getGenre().isEmpty()) {
                Join<Review, Movie> reviewMovieJoin = root.join("movie", JoinType.INNER);
                predicates.add(builder.equal(builder.upper(reviewMovieJoin.get("genre")), dto.getGenre().toUpperCase()));
            }*/
            if (dto.getReviewName() != null && !dto.getReviewName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + dto.getReviewName().toLowerCase() + "%"));
            }
            if (dto.getReviewOpinion() != null && !dto.getReviewOpinion().isEmpty()) {
                predicates.add(builder.equal(builder.upper(root.get("opinion")), dto.getReviewOpinion().toUpperCase()));
            }

           /* if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                *//*Join<Review, Movie> reviewMovieJoin = root.join("movie", JoinType.INNER);
                searchPredicatesList.add(
                        builder.like(builder.lower(reviewMovieJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(reviewMovieJoin.get("genre")), dto.getSearchString().toLowerCase() + "%"));*//*
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), dto.getSearchString().toLowerCase() + "%"));
            }*/

            //Predicate searchPredicate = builder.or(searchPredicatesList.toArray(new Predicate[0]));

            //if (searchPredicatesList.isEmpty()){
                return builder.and(predicates.toArray(new Predicate[0]));
            /*}
            return builder.and(filterPredicate, searchPredicate);*/
        };
    }
}
