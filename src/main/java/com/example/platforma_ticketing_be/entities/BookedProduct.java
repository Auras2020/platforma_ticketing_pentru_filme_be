/*
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

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(name = "number_products", nullable = false)
    private int numberProducts;

    @Column(name = "status", nullable = false)
    private String status;

    public BookedProduct(ShowTiming showTiming, UserAccount user, Product product, int number, String status) {
        this.showTiming = showTiming;
        this.user = user;
        this.product = product;
        this.number = number;
        this.status = status;
    }

    public BookedProduct() {

    }
}
*/
