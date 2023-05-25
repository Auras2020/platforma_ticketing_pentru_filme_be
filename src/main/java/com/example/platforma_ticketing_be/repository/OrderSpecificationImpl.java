package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.OrderFilterDto;
import com.example.platforma_ticketing_be.entities.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderSpecificationImpl {

    public Specification<Orders> getOrders(OrderFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            Join<Orders, ShowTiming> ordersShowTimingJoin = root.join("showTiming", JoinType.INNER);
            Join<ShowTiming, Theatre> showTimingTheatreJoin = ordersShowTimingJoin.join("theatre", JoinType.INNER);
            Join<ShowTiming, Movie> showTimingMovieJoin = ordersShowTimingJoin.join("movie", JoinType.INNER);

            if (dto.getTheatreLocation() != null && !dto.getTheatreLocation().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getTheatreLocation().toLowerCase() + "%"));
            }

            if (dto.getTheatreName() != null && !dto.getTheatreName().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getTheatreName().toLowerCase() + "%"));
            }

            if (dto.getMovieName() != null && !dto.getMovieName().isEmpty()) {
                predicates.add(builder.like(builder.lower(showTimingMovieJoin.get("name")), dto.getMovieName().toLowerCase() + "%"));
            }

            if (dto.getDay() != null) {
                predicates.add(builder.equal(ordersShowTimingJoin.get("day"), dto.getDay()));
            }

            if (dto.getProductName() != null && !dto.getProductName().isEmpty()) {
                Join<Orders, Product> ordersProductJoin = root.join("product", JoinType.INNER);
                predicates.add(builder.like(builder.lower(ordersProductJoin.get("name")), dto.getProductName().toLowerCase() + "%"));
            }

            if (dto.getTicketStatus() != null && !dto.getTicketStatus().isEmpty()) {
                predicates.add(builder.equal(root.get("ticketStatus"), dto.getTicketStatus()));
            }

            if (dto.getProductStatus() != null && !dto.getProductStatus().isEmpty()) {
                predicates.add(builder.equal(root.get("productsStatus"), dto.getProductStatus()));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingMovieJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
                Join<Orders, Product> ordersProductJoin = root.join("product", JoinType.INNER);
                searchPredicatesList.add(
                        builder.like(builder.lower(ordersProductJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
            }

            Predicate searchPredicate = builder.or(searchPredicatesList.toArray(new Predicate[0]));
            Predicate filterPredicate = builder.and(predicates.toArray(new Predicate[0]));

            if (searchPredicatesList.isEmpty()) {
                return filterPredicate;
            }

            return builder.and(filterPredicate, searchPredicate);
        };
    }
}
