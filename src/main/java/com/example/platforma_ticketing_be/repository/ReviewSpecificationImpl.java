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
            if (dto.getReviewName() != null && !dto.getReviewName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + dto.getReviewName().toLowerCase() + "%"));
            }
            if (dto.getReviewOpinion() != null && !dto.getReviewOpinion().isEmpty()) {
                predicates.add(builder.equal(builder.upper(root.get("opinion")), dto.getReviewOpinion().toUpperCase()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
