package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.*;
import com.example.platforma_ticketing_be.repository.ShowTimingRepository;
import com.example.platforma_ticketing_be.repository.ShowTimingSpecificationImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShowTimingService {

    private final ShowTimingRepository showTimingRepository;
    private final ModelMapper modelMapper;
    private final ShowTimingSpecificationImpl showTimingSpecification;
    @PersistenceContext
    private EntityManager entityManager;

    public ShowTimingService(ShowTimingRepository showTimingRepository, ModelMapper modelMapper, ShowTimingSpecificationImpl showTimingSpecification) {
        this.showTimingRepository = showTimingRepository;
        this.modelMapper = modelMapper;
        this.showTimingSpecification = showTimingSpecification;
    }

    private ShowTimingPageResponseDto getShowTimingPageResponse(Page<ShowTiming> pageOfShowTimings){
        List<ShowTiming> showTimings = pageOfShowTimings.getContent();
        List<ShowTimingDto> dtos = showTimings.stream()
                .map(showTiming -> this.modelMapper.map(showTiming, ShowTimingDto.class))
                .collect(Collectors.toList());
        return new ShowTimingPageResponseDto(dtos, pageOfShowTimings.getNumber(),
                (int) pageOfShowTimings.getTotalElements(), pageOfShowTimings.getTotalPages());
    }

    public ShowTimingPageResponseDto findAllByPaging(int page, int size) {
        Pageable pagingSort = PageRequest.of(page, size);
        Page<ShowTiming> pageOfShowTimings = this.showTimingRepository.findAll(pagingSort);
        return getShowTimingPageResponse(pageOfShowTimings);
    }

    public ShowTimingPageResponseDto findAllByPagingAndFilter(ShowTimingPageDto dto) {
        Pageable pagingSort = PageRequest.of(dto.getPage(), dto.getSize());
        Specification<ShowTiming> specification = this.showTimingSpecification.getShowTimings(dto.getDto());
        Page<ShowTiming> pageOfShowTimings = this.showTimingRepository.findAll(specification, pagingSort);
        return getShowTimingPageResponse(pageOfShowTimings);
    }

    public ShowTiming create(ShowTimingDto showTimingDto){
        ShowTiming showTiming = this.modelMapper.map(showTimingDto, ShowTiming.class);
        showTimingRepository.save(showTiming);
        return showTiming;
    }

    public void delete(Long id){
        Optional<ShowTiming> showTiming = showTimingRepository.findById(id);
        if(showTiming.isEmpty()){
            throw new EntityNotFoundException(ShowTiming.class.getSimpleName() + " with id: " + id);
        }
        showTimingRepository.deleteById(id);
    }

    public ShowTimingDto findShowTimingByShowTimingDetails(Long theatreId, Long movieId, Date day, String time){
        List<ShowTiming> showTimings = this.showTimingRepository.findShowTimingByShowTimingDetails(theatreId, movieId, time).stream()
                .filter(showTiming -> showTiming.getDay().getDate() == day.getDate() && showTiming.getDay().getMonth() == day.getMonth())
                .toList();
        return showTimings.isEmpty() ? null : this.modelMapper.map(showTimings.get(0), ShowTimingDto.class);
    }

    public ShowTimingDto getShowTimingById(Long id){
        if(this.showTimingRepository.findById(id).isPresent()){
            return this.modelMapper.map(this.showTimingRepository.findById(id).get(), ShowTimingDto.class);
        }
        return null;
    }

    public Set<Long> filterShowTimings(List<ShowTiming> showTimings, Specification<ShowTiming> specification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ShowTiming> query = builder.createQuery(ShowTiming.class);
        Root<ShowTiming> root = query.from(ShowTiming.class);

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        Set<Long> filteredShowTimings = new HashSet<>();
        for (ShowTiming showTiming: showTimings) {
            Predicate showTimingPredicate = builder.and(predicate, builder.equal(root, showTiming));
            query.where(showTimingPredicate);

            List<ShowTiming> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredShowTimings.add(showTiming.getId());
            }
        }

        return filteredShowTimings;
    }

    public ShowTimingPResponseDto getAllShowTimingsFromATheatre(TheatrePDto dto){
        List<ShowTiming> showTimings = this.showTimingRepository.getAllShowTimingsFromAGivenTheatre(PageRequest.of(dto.getPage(), dto.getSize()),
                dto.getTheatreId());
        List<ShowTimingDto> showTimingDtos = showTimings.stream()
                .map(showTiming -> this.modelMapper.map(showTiming, ShowTimingDto.class))
                .toList();

        int totalShowTimings = 0;
        if(this.showTimingRepository.findAll().size() > 0){
            totalShowTimings = this.showTimingRepository.getAllShowTimingsFromAGivenTheatre(PageRequest.of(0, this.showTimingRepository.findAll().size()), dto.getTheatreId()).size();
        }

        return new ShowTimingPResponseDto(showTimingDtos, totalShowTimings);
    }

    public ShowTimingPResponseDto getAllFilteredShowTimingsFromATheatre(ShowTimingFilterDto showTimingFilterDto, TheatrePDto dto){
        Specification<ShowTiming> specification = this.showTimingSpecification.getShowTimings(showTimingFilterDto);
        Set<Long> filteredShowTimings = filterShowTimings(this.showTimingRepository.findAll(), specification);
        int totalShowTimings = 0;
        List<ShowTimingDto> showTimingDtos = new ArrayList<>();
        if(this.showTimingRepository.findAll().size() > 0){
            Set<ShowTiming> showTimings = new HashSet<>(this.showTimingRepository.getAllFilteredShowTimingsFromAGivenTheatre(PageRequest.of(dto.getPage(), dto.getSize()),
                    dto.getTheatreId(), filteredShowTimings));
            showTimingDtos = showTimings.stream()
                    .map(showTiming -> this.modelMapper.map(showTiming, ShowTimingDto.class))
                    .toList();
            totalShowTimings = this.showTimingRepository.getAllFilteredShowTimingsFromAGivenTheatre(PageRequest.of(0, this.showTimingRepository.findAll().size()), dto.getTheatreId(), filteredShowTimings).size();
        }
        return new ShowTimingPResponseDto(showTimingDtos, totalShowTimings);
    }
}
