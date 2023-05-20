package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class BookedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="show_timing_id", nullable=false)
    private ShowTiming showTiming;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserAccount user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "status", nullable = false)
    private String status;

    public BookedProduct(ShowTiming showTiming, UserAccount user, String name, int quantity, int number, String status) {
        this.showTiming = showTiming;
        this.user = user;
        this.name = name;
        this.quantity = quantity;
        this.number = number;
        this.status = status;
    }

    public BookedProduct() {

    }
}
