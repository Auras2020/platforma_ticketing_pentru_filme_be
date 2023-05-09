package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.ShowTimingFilterDto;
import com.example.platforma_ticketing_be.dtos.VenueFilterDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.entities.Venue;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VenueSpecificationImpl {
    public Specification<Venue> getVenues(VenueFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            Join<Venue, Theatre> venueTheatreJoin = root.join("theatre", JoinType.INNER);
            if (dto.getLocation() != null && !dto.getLocation().isEmpty()) {
                predicates.add(builder.like(builder.lower(venueTheatreJoin.get("location")), dto.getLocation().toLowerCase() + "%"));
            }
            if (dto.getTheatreName() != null && !dto.getTheatreName().isEmpty()) {
                predicates.add(builder.like(builder.lower(venueTheatreJoin.get("name")), dto.getTheatreName().toLowerCase() + "%"));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                Join<ShowTiming, Theatre> showTimingTheatreJoin = root.join("theatre", JoinType.INNER);
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("location")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(showTimingTheatreJoin.get("name")), dto.getSearchString().toLowerCase() + "%"));
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
