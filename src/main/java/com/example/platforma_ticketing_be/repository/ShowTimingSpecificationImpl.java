package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.ShowTimingFilterDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.Theatre;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ShowTimingSpecificationImpl {
    public Specification<ShowTiming> getShowTimings(ShowTimingFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            Join<ShowTiming, Theatre> showTimingTheatreJoin = root.join("theatre", JoinType.INNER);
            if (dto.getTheatreLocation() != null && !dto.getTheatreLocation().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getTheatreLocation().toLowerCase() + "%"));
            }
            if (dto.getTheatreName() != null && !dto.getTheatreName().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getTheatreName().toLowerCase() + "%"));
            }
            if (dto.getMovieName() != null && !dto.getMovieName().isEmpty()) {
                Join<ShowTiming, Movie> showTimingMovieJoin = root.join("movie", JoinType.INNER);
                predicates.add(builder.like(builder.lower(showTimingMovieJoin.get("name")), dto.getMovieName().toLowerCase() + "%"));
            }
            if(dto.getStartDate() != null){
                predicates.add(builder.greaterThanOrEqualTo(root.get("startDate"), dto.getStartDate()));
            }
            if(dto.getEndDate() != null){
                predicates.add(builder.lessThanOrEqualTo(root.get("endDate"), dto.getEndDate()));
            }
            if(dto.getDay() != null){
                predicates.add(builder.equal(root.get("day"), dto.getDay()));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                //Join<ShowTiming, Theatre> showTimingTheatreJoin = root.join("theatre", JoinType.INNER);
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
                Join<ShowTiming, Movie> showTimingMovieJoin = root.join("movie", JoinType.INNER);
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingMovieJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
            }

            Predicate searchPredicate = builder.or(searchPredicatesList.toArray(new Predicate[0]));
            Predicate filterPredicate = builder.and(predicates.toArray(new Predicate[0]));

            if (searchPredicatesList.isEmpty()){
                return filterPredicate;
            }
            return builder.and(filterPredicate, searchPredicate);
        };
    }
}
