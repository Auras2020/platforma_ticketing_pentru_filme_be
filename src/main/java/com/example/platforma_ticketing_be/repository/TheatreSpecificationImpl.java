package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.TheatreFilterDto;
import com.example.platforma_ticketing_be.dtos.UserFilterDto;
import com.example.platforma_ticketing_be.entities.Theatre;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.entities.UserRole;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TheatreSpecificationImpl {

    public Specification<Theatre> getTheatres(TheatreFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            if (dto.getName() != null && !dto.getName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), dto.getName().toLowerCase() + "%"));
            }
            if (dto.getLocation() != null && !dto.getLocation().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("location")), dto.getLocation().toLowerCase() + "%"));
            }
            if (dto.getAddress() != null && !dto.getAddress().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("address")), "%" + dto.getAddress().toLowerCase() + "%"));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("location")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("address")), "%" + dto.getSearchString().toLowerCase() + "%"));
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
