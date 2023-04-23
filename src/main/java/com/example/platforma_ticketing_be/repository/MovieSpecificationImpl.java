package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.MovieFilterDto;
import com.example.platforma_ticketing_be.entities.Movie;
import com.example.platforma_ticketing_be.entities.MovieGenre;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MovieSpecificationImpl {

    private String getAgeLimit(String ageLimit){
        if(ageLimit.contains("<")){
            return "<";
        } else {
            return ageLimit.substring(2);
        }
    }

    private boolean checkIfMovieGenreExists(String genre) {
        for (MovieGenre movieGenre : MovieGenre.values()) {
            if (movieGenre.name().equals(genre))
                return true;
        }
        return false;
    }

    private int parseTime(String timeStr) {
        int hours = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("h")));
        int minutes = Integer.parseInt(timeStr.substring(timeStr.indexOf("h")+1, timeStr.indexOf("m")));
        return hours * 60 + minutes;
    }

    private int getLeftDurationInterval(String interval){
        if(interval.contains("<") || interval.contains(">")){
            return parseTime(interval.substring(1));
        } else {
            String[] times = interval.split("-");
            return parseTime(times[0]);
        }
    }

    private int getRightDurationInterval(String interval){
        if(interval.contains("<") || interval.contains(">")){
            return parseTime(interval.substring(1));
        } else {
            String[] times = interval.split("-");
            return parseTime(times[1]);
        }
    }

    public Specification<Movie> getMovies(MovieFilterDto dto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> searchPredicatesList = new ArrayList<>();

            if (dto.getName() != null && !dto.getName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), dto.getName().toLowerCase() + "%"));
            }
            if (dto.getRecommendedAge() != null && !dto.getRecommendedAge().isEmpty()) {
                if(getAgeLimit(dto.getRecommendedAge()).equals("<")){
                    predicates.add(builder.lt(root.get("recommended_age"), 12));
                } else {
                    int ageLimit = Integer.parseInt(getAgeLimit(dto.getRecommendedAge()));
                    predicates.add(builder.ge(root.get("recommended_age"), ageLimit));
                }
            }
            if (dto.getGenre() != null && !dto.getGenre().isEmpty()) {
                if (checkIfMovieGenreExists(dto.getGenre().toUpperCase())){
                    predicates.add(
                            builder.equal(root.get("genre"), dto.getGenre().toUpperCase()));
                } else{
                    throw new EnumConstantNotPresentException(
                            MovieGenre.class, dto.getGenre() + " does not exists");
                }
            }
            if (dto.getDuration() != null && !dto.getDuration().isEmpty()) {
                if(dto.getDuration().contains("<")){
                    predicates.add(builder.lt(root.get("duration"), getLeftDurationInterval(dto.getDuration())));
                } else if(dto.getDuration().contains(">")){
                    predicates.add(builder.gt(root.get("duration"), getLeftDurationInterval(dto.getDuration())));
                } else {
                    predicates.add(builder.ge(root.get("age"), getLeftDurationInterval(dto.getDuration())));
                    predicates.add(builder.le(root.get("age"), getRightDurationInterval(dto.getDuration())));
                }
            }
            if (dto.getActors() != null && !dto.getActors().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("actors")), dto.getActors().toLowerCase() + "%"));
            }
            if (dto.getDirector() != null && !dto.getDirector().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("director")), dto.getDirector().toLowerCase() + "%"));
            }
            if (dto.getSynopsis() != null && !dto.getSynopsis().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("synopsis")), dto.getSynopsis().toLowerCase() + "%"));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("genre")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("actors")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("director")), dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("synopsis")), dto.getSearchString().toLowerCase() + "%"));
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
