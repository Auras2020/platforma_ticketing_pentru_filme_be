package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.dtos.MovieFilterDto;
import com.example.platforma_ticketing_be.entities.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MovieSpecificationImpl {

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
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + dto.getName().toLowerCase() + "%"));
            }
            if (dto.getRecommendedAge() != null && !dto.getRecommendedAge().isEmpty()) {
                predicates.add(builder.equal((root.get("recommendedAge")), dto.getRecommendedAge()));
            }
            if (dto.getGenre() != null && dto.getGenre().length > 0) {
                Join<Movie, MovieGenres> movieMovieGenresJoin = root.join("movieGenres", JoinType.INNER);
                Join<MovieGenres, Genre> movieGenresGenreJoin = movieMovieGenresJoin.join("genre", JoinType.INNER);
                Expression<String> genreExpression = movieGenresGenreJoin.get("name");
                String[] genres = dto.getGenre();
                List<Predicate> genrePredicates = new ArrayList<>();

                for (String genre : genres) {
                    if (checkIfMovieGenreExists(genre.toUpperCase())) {
                        genrePredicates.add(builder.equal(builder.upper(genreExpression), genre.toUpperCase()));
                    } else {
                        throw new EnumConstantNotPresentException(MovieGenre.class, genre + " does not exist");
                    }
                }

                Predicate genrePredicate = builder.or(genrePredicates.toArray(new Predicate[0]));
                predicates.add(genrePredicate);

                query.distinct(true);
            }
            if (dto.getDuration() != null && !dto.getDuration().isEmpty()) {
                if(dto.getDuration().contains("<")){
                    predicates.add(builder.lt(root.get("duration"), getLeftDurationInterval(dto.getDuration())));
                } else if(dto.getDuration().contains(">")){
                    predicates.add(builder.gt(root.get("duration"), getLeftDurationInterval(dto.getDuration())));
                } else {
                    predicates.add(builder.ge(root.get("duration"), getLeftDurationInterval(dto.getDuration())));
                    predicates.add(builder.le(root.get("duration"), getRightDurationInterval(dto.getDuration())));
                }
            }
            if (dto.getActors() != null && !dto.getActors().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("actors")), "%" + dto.getActors().toLowerCase() + "%"));
            }
            if (dto.getDirector() != null && !dto.getDirector().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("director")), "%" + dto.getDirector().toLowerCase() + "%"));
            }
            if (dto.getSynopsis() != null && !dto.getSynopsis().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("synopsis")), "%" + dto.getSynopsis().toLowerCase() + "%"));
            }

            if ((dto.getSearchString() != null) && !(dto.getSearchString().isEmpty())) {
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("name")), "%" + dto.getSearchString().toLowerCase() + "%"));

                Join<Movie, MovieGenres> movieMovieGenresJoin = root.join("movieGenres", JoinType.INNER);
                Join<MovieGenres, Genre> movieGenresGenreJoin = movieMovieGenresJoin.join("genre", JoinType.INNER);
                searchPredicatesList.add(
                        builder.like(builder.lower(movieGenresGenreJoin.get("name")), "%" + dto.getSearchString().toLowerCase() + "%"));
                query.distinct(true);

                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("actors")), "%" + dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("director")), "%" + dto.getSearchString().toLowerCase() + "%"));
                searchPredicatesList.add(
                        builder.like(builder.lower(root.get("synopsis")), "%" + dto.getSearchString().toLowerCase() + "%"));
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
