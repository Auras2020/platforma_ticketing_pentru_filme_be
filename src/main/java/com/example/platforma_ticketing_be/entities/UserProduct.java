package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="product_name", nullable=false)
    private String productName;

    @Column(name="quantity", nullable=false)
    private int quantity;

    @Column(name="number", nullable=false)
    private int number;

    @ManyToOne
    @JoinColumn(name="show_timing_id", nullable=false)
    private ShowTiming showTiming;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserAccount user;
}
