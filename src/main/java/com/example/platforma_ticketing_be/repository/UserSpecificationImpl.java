package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.UserFilterDto;
import com.example.platforma_ticketing_be.entities.Role;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.entities.UserRole;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserSpecificationImpl {

    private boolean checkIfUserRoleExists(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(role))
                return true;
        }
        return false;
    }

    public Specification<UserAccount> getUsers(UserFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            if (dto.getRole() != null && !dto.getRole().isEmpty()) {
                if (checkIfUserRoleExists(dto.getRole().toUpperCase())){
                    predicates.add(
                            builder.equal(root.get("role"), dto.getRole().toUpperCase()));
                } else{
                    throw new EnumConstantNotPresentException(
                            UserRole.class, dto.getRole() + " does not exists");
                }
            }
            if (dto.getName() != null && !dto.getName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), dto.getName().toLowerCase() + "%"));
            }
            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("email")), dto.getEmail().toLowerCase() + "%"));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("email")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("role")), dto.getSearchString().toLowerCase() + "%"));
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
