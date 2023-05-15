package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.MovieFilterDto;
import com.example.platforma_ticketing_be.dtos.ProductFilterDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.MovieGenre;
import com.example.platforma_ticketing_be.entities.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecificationImpl {

    public Specification<Product> getProducts(ProductFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> searchPredicatesList = new ArrayList<>();

            if ((dto.getSearchString() != null)/* && !(dto.getSearchString().isEmpty())*/) {
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), dto.getSearchString().toLowerCase() + "%"));
            }

            Predicate searchPredicate =  builder.or(searchPredicatesList.toArray(new Predicate[0]));
            return searchPredicate;
        };
    }
}
