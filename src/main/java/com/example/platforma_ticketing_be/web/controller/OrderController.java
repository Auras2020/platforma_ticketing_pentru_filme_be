package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.service.OrderService;
import com.lowagie.text.DocumentException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
    public void getAllOrdersPage(
            @RequestBody OrdersDto dto) {
        this.orderService.changeOrdersStatus(dto);
    }

    @PostMapping("/details")
    public List<ProductDetailsDto> getBookedProductsDetails(@RequestBody OrdersDto ordersDto){
        return this.orderService.getBookedProductsDetails(ordersDto);
    }

    @PutMapping()
    public void createOrder(@RequestBody TicketsProductsDto ticketsProductsDto) throws DocumentException, IOException {
        this.orderService.createOrder(ticketsProductsDto.getShowTiming(), ticketsProductsDto.getSeats(),
                ticketsProductsDto.getProductDetails(), ticketsProductsDto.getUser(),
                ticketsProductsDto.getTicketStatus(), ticketsProductsDto.getProductStatus());
    }

    @GetMapping("/{id}")
    public Set<String> findSeatsByShowTiming(@PathVariable("id") Long id){
        return this.orderService.findSeatsByShowTimingId(id);
    }
}
