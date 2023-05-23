/*
package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.BookedProduct;
import com.example.platforma_ticketing_be.entities.Product;
import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.repository.*;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookedProductService {

    private final BookedProductRepository bookedProductRepository;
    private final ModelMapper modelMapper;
    private final BookedProductSpecificationImpl bookedProductSpecification;
    private final SeatSpecificationImpl seatSpecification;
    private final EmailServiceImpl emailService;
    private final ProductRepository productRepository;
    private final ShowTimingRepository showTimingRepository;
    private final SeatRepository seatRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public BookedProductService(BookedProductRepository bookedProductRepository, ModelMapper modelMapper, BookedProductSpecificationImpl bookedProductSpecification, SeatSpecificationImpl seatSpecification, EmailServiceImpl emailService, ProductRepository productRepository, ShowTimingRepository showTimingRepository, SeatRepository seatRepository) {
        this.bookedProductRepository = bookedProductRepository;
        this.modelMapper = modelMapper;
        this.bookedProductSpecification = bookedProductSpecification;
        this.seatSpecification = seatSpecification;
        this.emailService = emailService;
        this.productRepository = productRepository;
        this.showTimingRepository = showTimingRepository;
        this.seatRepository = seatRepository;
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

    public OrderPageResponseDto getAllBookedProductsByPaging(BookedProductPDto dto) {
        List<Object[]> objects = this.bookedProductRepository
                .findBookProductsOfAUser(
                        PageRequest.of(dto.getPage(), dto.getSize()),
                        dto.getUser().getId());
        List<OrdersDto> ordersDtos = new ArrayList<>();
        for(Object[] object: objects){
            ShowTiming showTiming = this.showTimingRepository.findById((long) (int) object[0]).get();
            BigInteger nrProducts = (BigInteger) object[1];
            String productsStatus = (String) object[2];
            BigInteger nrTickets = (BigInteger) object[1];
            String ticketsStatus = (String) object[2];
            ordersDtos.add(new OrdersDto(this.modelMapper.map(showTiming, ShowTimingDto.class), dto.getUser(), nrTickets, ticketsStatus, nrProducts, productsStatus));
        }
        int totalBookedProducts = 0;
        if(this.bookedProductRepository.findAll().size() > 0){
            totalBookedProducts = this.bookedProductRepository.findBookProductsOfAUser(PageRequest.of(0, this.bookedProductRepository.findAll().size()), dto.getUser().getId()).size();
        }
        return new OrderPageResponseDto(ordersDtos, totalBookedProducts);
    }

    public OrderPageResponseDto getAllBookedProductsByPagingAndFilter(BookedProductPageDto dto) {
        Set<Long> filteredBookedProducts1 = filterBookedProducts(this.bookedProductRepository.findAll(), dto);
        int totalBookedProducts = 0;
        List<OrdersDto> filteredOrders = new ArrayList<>();
        if(this.bookedProductRepository.findAll().size() > 0){
            List<Object[]> objects = this.bookedProductRepository
                .findFilteredBookProductsOfAUser(
                        PageRequest.of(dto.getPage(), dto.getSize()),
                        dto.getUser().getId(), filteredBookedProducts1);
            for(Object[] object: objects){
                ShowTiming showTiming = this.showTimingRepository.findById((long) (int) object[0]).get();
                BigInteger nrTickets = (BigInteger) object[1];
                String ticketsStatus = (String) object[2];
                BigInteger nrProducts = (BigInteger) object[3];
                String productsStatus = (String) object[4];
                filteredOrders.add(new OrdersDto(this.modelMapper.map(showTiming, ShowTimingDto.class), dto.getUser(), nrTickets, ticketsStatus, nrProducts, productsStatus));
            }
            totalBookedProducts = this.bookedProductRepository.findFilteredBookProductsOfAUser(PageRequest.of(0, this.bookedProductRepository.findAll().size()), dto.getUser().getId(), filteredBookedProducts1).size();
        }
        return new OrderPageResponseDto(filteredOrders, totalBookedProducts);
    }

    public void changeBookedProductsStatus(BookedProductsDto bookedProductsDto){
        List<BookedProduct> bookedProducts = this.bookedProductRepository.findBookedProductByUserIdAndShowTimingId(bookedProductsDto.getUser().getId(), bookedProductsDto.getShowTiming().getId());
        for(BookedProduct bookedProduct: bookedProducts){
            bookedProduct.setStatus(bookedProductsDto.getStatus());
            bookedProductRepository.save(bookedProduct);
        }
        if(bookedProductsDto.getStatus().equals("cancelled")){
            refreshNumberOfAvailableProductsInTheatre(bookedProductsDto);
        }
        sendEmailWithBookedproductsStatus(bookedProductsDto);
    }

    public List<ProductDetailsDto> getBookedProductsDetails(BookedProductsDto bookedProductsDto){
        List<BookedProduct> bookedProducts = this.bookedProductRepository.findBookedProductByUserIdAndShowTimingId(bookedProductsDto.getUser().getId(), bookedProductsDto.getShowTiming().getId());
        List<ProductDetailsDto> productDetailsDtos = new ArrayList<>();
        for(BookedProduct bookedProduct: bookedProducts){
            productDetailsDtos.add(new ProductDetailsDto(bookedProduct.getProduct().getName(), bookedProduct.getProduct().getPrice(),
                    bookedProduct.getProduct().getQuantity(), bookedProduct.getNumber()));
        }
        return productDetailsDtos;
    }

    private String changeDateFormat(Date date){
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    private void sendEmailWithBookedproductsStatus(BookedProductsDto bookedProductsDto){
        String subject = "Hot Movies Center - " + bookedProductsDto.getStatus().substring(0, 1).toUpperCase() + bookedProductsDto.getStatus().substring(1) + " Products";
        String body = "Your products for movie " + bookedProductsDto.getShowTiming().getMovie().getName() +
                " at theatre " + bookedProductsDto.getShowTiming().getTheatre().getName() + " on date of " +
                changeDateFormat(bookedProductsDto.getShowTiming().getDay()) + " " +
                bookedProductsDto.getShowTiming().getTime() + " were " + bookedProductsDto.getStatus();
        this.emailService.sendEmail(subject, body, bookedProductsDto.getUser().getEmail());
    }

    private void refreshNumberOfAvailableProductsInTheatre(BookedProductsDto bookedProductsDto){
        List<BookedProduct> bookedProducts = this.bookedProductRepository.findBookedProductByUserIdAndShowTimingId(bookedProductsDto.getUser().getId(), bookedProductsDto.getShowTiming().getId());
        for(BookedProduct bookedProduct: bookedProducts){
            Product product = bookedProduct.getProduct();
            product.setNumber(product.getNumber() + bookedProduct.getNumber());
            this.productRepository.save(product);
        }
    }
}
*/
