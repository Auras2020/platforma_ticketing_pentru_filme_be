package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {

    @Query("SELECT st, o.ticketStatus, SUM(o.numberProducts), o.productsStatus, o.createdDate " +
            "FROM show_schedule st " +
            "INNER JOIN Orders o ON o.showTiming.id = st.id " +
            "WHERE o.user.id = :id " +
            "GROUP BY st, o.createdDate, o.ticketStatus, o.productsStatus " +
            "ORDER BY st.day desc, st.time, o.createdDate desc")
    List<Object[]> findOrdersOfAUser(Pageable pageable, Long id);

    @Query("SELECT st, o.ticketStatus, SUM(o.numberProducts), o.productsStatus, o.createdDate " +
            "FROM show_schedule st " +
            "INNER JOIN Orders o ON o.showTiming.id = st.id " +
            "WHERE o.user.id = :id and o.id in :ids " +
            "GROUP BY st, o.createdDate, o.ticketStatus, o.productsStatus " +
            "ORDER BY st.day desc, st.time, o.createdDate desc")
    List<Object[]> findFilteredOrdersOfAUser(Pageable pageable, Long id, Set<Long> ids);

    @Query("SELECT o FROM Orders o WHERE o.showTiming.id = ?1 AND o.ticketsPrice > 0 AND o.createdDate = ?2")
    Orders findOrderByShowTiming(Long showTimingId, Date createdDate);

    List<Orders> findOrdersByUserIdAndShowTimingIdAndCreatedDate(Long userId, Long showTimingId, Date date);

    @Query("SELECT o.seat, o.ticketStatus " +
            "FROM Orders o " +
            "WHERE o.showTiming.id = ?1 AND " +
            "o.createdDate = (SELECT MAX(createdDate) FROM Orders WHERE showTiming.id = ?1 AND seat = o.seat AND ticketStatus = o.ticketStatus)")
    List<Object[]> findSeatsAndTicketsStatusByShowTimingId(Long id);

    @Query("SELECT DISTINCT MAX(o.createdDate) FROM Orders o WHERE o.user.id = ?1 AND o.showTiming.id = ?2")
    Date getLastOrderCreatedByUserAndShowTiming(Long userId, Long showTimingId);

    @Query("SELECT o.showTiming.movie.name, COUNT(o.showTiming.movie.id) AS number_of_tickets " +
            "FROM Orders o " +
            "WHERE o.seat IS NOT NULL AND o.ticketStatus <> 'cancelled' " +
            "GROUP BY o.showTiming.movie.name " +
            "ORDER BY number_of_tickets desc")
    List<Object[]> findNumberOfTicketsPerMovie();

    @Query("SELECT o.showTiming.movie.name, COUNT(o.showTiming.movie.id) AS number_of_tickets " +
            "FROM Orders o " +
            "WHERE o.showTiming.theatre.id = ?1 AND o.seat IS NOT NULL AND o.ticketStatus <> 'cancelled' " +
            "GROUP BY o.showTiming.movie.name " +
            "ORDER BY number_of_tickets desc")
    List<Object[]> findNumberOfTicketsPerMovieFromGivenTheatre(Long theatreId);

    @Query("SELECT o.showTiming.movie.name, SUM(o.ticketsPrice) AS price_of_tickets " +
            "FROM Orders o " +
            "WHERE o.ticketStatus <> 'cancelled' " +
            "GROUP BY o.showTiming.movie.name " +
            "ORDER BY price_of_tickets desc")
    List<Object[]> findPriceOfTicketsPerMovie();

    @Query("SELECT o.showTiming.movie.name, SUM(o.ticketsPrice) AS price_of_tickets " +
            "FROM Orders o " +
            "WHERE o.showTiming.theatre.id = ?1 AND o.ticketStatus <> 'cancelled' " +
            "GROUP BY o.showTiming.movie.name " +
            "ORDER BY price_of_tickets desc")
    List<Object[]> findPriceOfTicketsPerMovieFromGivenTheatre(Long theatreId);

    @Query("SELECT o.product.name, SUM(o.numberProducts) AS number_of_products " +
            "FROM Orders o " +
            "WHERE o.product.id IS NOT NULL AND o.productsStatus <> 'cancelled' " +
            "GROUP BY o.product.name " +
            "ORDER BY number_of_products desc")
    List<Object[]> findNumberOfProductsSold();

    @Query("SELECT o.product.name, SUM(o.numberProducts) AS number_of_products " +
            "FROM Orders o " +
            "WHERE o.showTiming.theatre.id = ?1 AND o.product.id IS NOT NULL AND o.productsStatus <> 'cancelled' " +
            "GROUP BY o.product.name " +
            "ORDER BY number_of_products desc")
    List<Object[]> findNumberOfProductsSoldFromGivenTheatre(Long theatreId);

    @Query("SELECT COUNT(o.id) FROM Orders o WHERE o.ticketStatus <> 'cancelled' AND o.seat IS NOT NULL")
    Integer getNumberOfTicketsSold();

    @Query("SELECT COUNT(o.id) FROM Orders o WHERE o.showTiming.theatre.id = ?1 AND o.ticketStatus <> 'cancelled' AND o.seat IS NOT NULL")
    Integer getNumberOfTicketsSoldFromATheatre(Long theatreId);

    @Query("SELECT SUM(o.numberProducts) FROM Orders o WHERE o.productsStatus <> 'cancelled' AND o.product.id IS NOT NULL")
    Integer getNumberOfProductsSold();

    @Query("SELECT SUM(o.numberProducts) FROM Orders o WHERE o.showTiming.theatre.id = ?1 AND o.productsStatus <> 'cancelled' AND o.product.id IS NOT NULL")
    Integer getNumberOfProductsSoldFromATheatre(Long theatreId);
}
