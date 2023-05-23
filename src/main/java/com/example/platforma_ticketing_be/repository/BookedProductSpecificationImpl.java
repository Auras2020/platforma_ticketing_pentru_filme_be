/*
package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.BookedProductFilterDto;
import com.example.platforma_ticketing_be.entities.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookedProductSpecificationImpl {

    public Specification<BookedProduct> getBookedProducts(BookedProductFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            Join<BookedProduct, Product> bookedproductProductJoin = root.join("product", JoinType.INNER);
            Join<BookedProduct, ShowTiming> bookedproductShowTimingJoin = root.join("showTiming", JoinType.INNER);
            Join<ShowTiming, Theatre> showTimingTheatreJoin = bookedproductShowTimingJoin.join("theatre", JoinType.INNER);
            Join<ShowTiming, Movie> showTimingMovieJoin = bookedproductShowTimingJoin.join("movie", JoinType.INNER);
            if (dto.getTheatreLocation() != null && !dto.getTheatreLocation().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getTheatreLocation().toLowerCase() + "%"));
            }
            if (dto.getTheatreName() != null && !dto.getTheatreName().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getTheatreName().toLowerCase() + "%"));
            }
            if (dto.getMovieName() != null && !dto.getMovieName().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingMovieJoin.get("name")), dto.getMovieName().toLowerCase() + "%"));
            }
            if(dto.getDay() != null){
                predicates.add(builder.equal(bookedproductShowTimingJoin.get("day"), dto.getDay()));
            }
            if (dto.getName() != null && !dto.getName().isEmpty()) {
                predicates.add(builder.like(builder.lower(bookedproductProductJoin.get("name")), dto.getName().toLowerCase() + "%"));
            }
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                predicates.add(builder.equal((root.get("status")), dto.getStatus()));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingMovieJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));

                searchPredicatesList.add(
                        builder.like(builder.lower(bookedproductProductJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("status")), dto.getSearchString().toLowerCase() + "%"));
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
*/
