package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.UserFilterDto;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.entities.UserRole;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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

    private String getLeftAgeInterval(String ageInterval){
        if(ageInterval.contains("-")){
            String[] ageValues = ageInterval.split("-");
            return ageValues[0];
        } else if(ageInterval.contains("<")){
            return "<";
        } else {
            return ">";
        }
    }

    private Integer getRightAgeInterval(String ageInterval){
        if(ageInterval.contains("-")){
            String[] ageValues = ageInterval.split("-");
            return Integer.parseInt(ageValues[1]);
        } else if(ageInterval.contains("<")){
            return 12;
        } else {
            return 18;
        }
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
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + dto.getName().toLowerCase() + "%"));
            }
            if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("email")), "%" + dto.getEmail().toLowerCase() + "%"));
            }
            if (dto.getAgeInterval() != null && !dto.getAgeInterval().isEmpty()) {
                if(getLeftAgeInterval(dto.getAgeInterval()).equals("<")){
                    predicates.add(builder.lt(root.get("age"), getRightAgeInterval(dto.getAgeInterval())));
                } else if(getLeftAgeInterval(dto.getAgeInterval()).equals(">")){
                    predicates.add(builder.ge(root.get("age"), getRightAgeInterval(dto.getAgeInterval())));
                } else {
                    predicates.add(builder.ge(root.get("age"), Integer.parseInt(getLeftAgeInterval(dto.getAgeInterval()))));
                    predicates.add(builder.lt(root.get("age"), getRightAgeInterval(dto.getAgeInterval())));
                }
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), "%" + dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("email")), "%" + dto.getSearchString().toLowerCase() + "%"));
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
