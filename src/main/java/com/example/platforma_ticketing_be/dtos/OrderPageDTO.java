package com.example.platforma_ticketing_be.dtos;

import com.example.platforma_ticketing_be.entities.UserAccount;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPageDTO {
    private UserAccount user;
    private OrderFilterDto dto;
    private int page;
    private int size;
}
