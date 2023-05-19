package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.BookedProduct;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.repository.BookedProductRepository;
import com.example.platforma_ticketing_be.repository.BookedProductSpecificationImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class BookedProductService {

    private final BookedProductRepository bookedProductRepository;
    private final ModelMapper modelMapper;
    private final BookedProductSpecificationImpl bookedProductSpecification;
    @PersistenceContext
    private EntityManager entityManager;

    public BookedProductService(BookedProductRepository bookedProductRepository, ModelMapper modelMapper, BookedProductSpecificationImpl bookedProductSpecification) {
        this.bookedProductRepository = bookedProductRepository;
        this.modelMapper = modelMapper;
        this.bookedProductSpecification = bookedProductSpecification;
    }

    public Set<Long> filterBookedProducts(List<BookedProduct> bookedProducts, BookedProductPageDto dto) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookedProduct> query = builder.createQuery(BookedProduct.class);
        Root<BookedProduct> root = query.from(BookedProduct.class);
        Specification<BookedProduct> specification = this.bookedProductSpecification.getBookedProducts(dto.getDto());

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        Set<Long> filteredBookedProducts = new HashSet<>();
        for (BookedProduct bookedProduct : bookedProducts) {
            Predicate bookedProductPredicate = builder.and(predicate, builder.equal(root, bookedProduct));
            query.where(bookedProductPredicate);

            List<BookedProduct> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredBookedProducts.add(bookedProduct.getShowTiming().getId());
            }
        }

        return filteredBookedProducts;
    }

    public BookedProductPageResponseDto getAllBookedProductsByPaging(BookedProductPDto dto) {
        List<Object[]> objects = this.bookedProductRepository
                .findBookProductsOfAUser(
                        PageRequest.of(dto.getPage(), dto.getSize()),
                        dto.getUser().getId());
        List<BookedProductsDto> bookedProductsDtos = new ArrayList<>();
        for(Object[] object: objects){
            ShowTiming showTiming = (ShowTiming) object[0];
            long nr = (long) object[1];
            String status = (String) object[2];
            bookedProductsDtos.add(new BookedProductsDto(this.modelMapper.map(showTiming, ShowTimingDto.class), dto.getUser(), (int) nr, status));
        }
        int totalBookedProducts = 0;
        if(this.bookedProductRepository.findAll().size() > 0){
            totalBookedProducts = this.bookedProductRepository.findBookProductsOfAUser(PageRequest.of(0, this.bookedProductRepository.findAll().size()), dto.getUser().getId()).size();
        }
        return new BookedProductPageResponseDto(bookedProductsDtos, totalBookedProducts);
    }

    public BookedProductPageResponseDto getAllBookedProductsByPagingAndFilter(BookedProductPageDto dto) {
        Set<Long> filteredBookedProducts1 = filterBookedProducts(this.bookedProductRepository.findAll(), dto);
        int totalBookedProducts = 0;
        List<BookedProductsDto> filteredBookedProducts = new ArrayList<>();
        if(this.bookedProductRepository.findAll().size() > 0){
            List<Object[]> objects = this.bookedProductRepository
                .findFilteredBookProductsOfAUser(
                        PageRequest.of(dto.getPage(), dto.getSize()),
                        dto.getUser().getId(), filteredBookedProducts1);
            for(Object[] object: objects){
                ShowTiming showTiming = (ShowTiming) object[0];
                long nr = (long) object[1];
                String status = (String) object[2];
                filteredBookedProducts.add(new BookedProductsDto(this.modelMapper.map(showTiming, ShowTimingDto.class), dto.getUser(), (int) nr, status));
            }
            totalBookedProducts = this.bookedProductRepository.findFilteredBookProductsOfAUser(PageRequest.of(0, this.bookedProductRepository.findAll().size()), dto.getUser().getId(), filteredBookedProducts1).size();
        }
        return new BookedProductPageResponseDto(filteredBookedProducts, totalBookedProducts);
    }

}
