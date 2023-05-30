package com.example.platforma_ticketing_be.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "tickets_price")
    private float ticketsPrice;

    @Column(name = "products_price")
    private float productsPrice;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public Orders(ShowTiming showTiming, UserAccount user, String seat, String ticketStatus, Product product,
                  int numberProducts, String productsStatus,
                  float ticketsPrice, float productsPrice, Date createdDate) {
        this.showTiming = showTiming;
        this.user = user;
        this.seat = seat;
        this.ticketStatus = ticketStatus;
        this.product = product;
        this.numberProducts = numberProducts;
        this.productsStatus = productsStatus;
        this.ticketsPrice = ticketsPrice;
        this.productsPrice = productsPrice;
        this.createdDate = createdDate;
    }
}
