package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Orders {
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

    @Column(name = "seat")
    private String seat;

    @Column(name = "ticket_status")
    private String ticketStatus;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "number_products")
    private int numberProducts;

    @Column(name = "products_status")
    private String productsStatus;

    public Orders(ShowTiming showTiming, UserAccount user, String seat, String ticketStatus, Product product, int numberProducts, String productsStatus) {
        this.showTiming = showTiming;
        this.user = user;
        this.seat = seat;
        this.ticketStatus = ticketStatus;
        this.product = product;
        this.numberProducts = numberProducts;
        this.productsStatus = productsStatus;
    }
}
