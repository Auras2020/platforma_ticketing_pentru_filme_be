package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.service.OrderService;
import com.lowagie.text.DocumentException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/page/filter")
    public OrderPageResponseDto getAllOrdersBySpecsPage(
            @RequestBody OrderPageDTO dto) {
        return this.orderService.getAllOrdersByPagingAndFilter(dto);
    }

    @GetMapping("/page")
    public OrderPageResponseDto getAllOrdersPage(
            @RequestBody OrderPDto dto) {
        return this.orderService.getAllOrdersByPaging(dto);
    }

    @PostMapping("/status")
    public void changeOrdersStatus(
            @RequestBody OrdersDto dto) {
        this.orderService.changeOrdersStatus(dto);
    }

    @PostMapping("/tickets-details")
    public List<TicketDetailsDto> getTicketsDetails(@RequestBody OrdersDto ordersDto){
        return this.orderService.getTicketsDetails(ordersDto);
    }

    @PostMapping("/products-details")
    public List<ProductDetailsDto> getBookedProductsDetails(@RequestBody OrdersDto ordersDto){
        return this.orderService.getBookedProductsDetails(ordersDto);
    }

    @PutMapping()
    public void createOrder(@RequestBody TicketsProductsDto ticketsProductsDto) throws DocumentException, IOException {
        this.orderService.createOrder(ticketsProductsDto.getShowTiming(), ticketsProductsDto.getSeats(),
                ticketsProductsDto.getProductDetails(), ticketsProductsDto.getUser(),
                ticketsProductsDto.getTicketStatus(), ticketsProductsDto.getProductStatus(),
                ticketsProductsDto.getNrAdults(), ticketsProductsDto.getNrStudents(), ticketsProductsDto.getNrChilds(),
                ticketsProductsDto.getTicketsPrice(), ticketsProductsDto.getProductsPrice(),
                ticketsProductsDto.getTicketsDiscount(), ticketsProductsDto.getProductsDiscount());
    }

    @GetMapping("/{id}")
    public List<SeatTicketStatusDto> findSeatsAndTicketsStatusByShowTiming(@PathVariable("id") Long id){
        return this.orderService.findSeatsAndTicketsStatusByShowTiming(id);
    }

    @PostMapping("/last-created-date")
    public Date getLastOrderCreatedByUserAndShowTiming(@RequestBody UserShowTimingDto userShowTimingDto) {
        return this.orderService.getLastOrderCreatedByUserAndShowTiming(userShowTimingDto);
    }

    @GetMapping("/tickets-number-chart")
    public List<TicketsNrDto> getTicketsNumber(){
        return this.orderService.getTicketsNumber();
    }

    @GetMapping("/tickets-price-chart")
    public List<TicketsPriceDto> getTicketsPrice(){
        return this.orderService.getTicketsPrice();
    }

    @GetMapping("/products-number-chart")
    public List<ProductsNrDto> getProductsNumber(){
        return this.orderService.getProductsNumber();
    }

   /* @GetMapping("/products-price-chart")
    public List<ProductsPriceDto> getProductsPrice(){
        return this.orderService.getProductsPrice();
    }*/
}
