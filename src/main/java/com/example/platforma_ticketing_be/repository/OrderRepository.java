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

    @Query("SELECT st, COUNT(o.seat), o.ticketStatus, SUM(o.numberProducts), o.productsStatus, o.createdDate  " +
            "FROM ShowTiming st " +
            "INNER JOIN Orders o ON o.showTiming.id = st.id " +
            "WHERE o.user.id = :id " +
            "GROUP BY st, o.createdDate, o.ticketStatus, o.productsStatus " +
            "ORDER BY st.day desc, st.time desc")
    List<Object[]> findOrdersOfAUser(Pageable pageable, Long id);

    @Query("SELECT st, COUNT(o.seat), o.ticketStatus, SUM(o.numberProducts), o.productsStatus, o.createdDate  " +
            "FROM ShowTiming st " +
            "INNER JOIN Orders o ON o.showTiming.id = st.id " +
            "WHERE o.user.id = :id and o.id in :ids " +
            "GROUP BY st, o.createdDate, o.ticketStatus, o.productsStatus " +
            "ORDER BY st.day desc, st.time desc")
    List<Object[]> findFilteredOrdersOfAUser(Pageable pageable, Long id, Set<Long> ids);

    List<Orders> findOrdersByUserIdAndShowTimingIdAndCreatedDate(Long userId, Long showTimingId, Date date);

    @Query("SELECT o.seat, o.ticketStatus " +
            "FROM Orders o " +
            "WHERE o.showTiming.id = ?1 AND " +
            "o.createdDate = (SELECT MAX(createdDate) FROM Orders WHERE showTiming.id = ?1 AND seat = o.seat AND ticketStatus = o.ticketStatus)")
    List<Object[]> findSeatsAndTicketsStatusByShowTimingId(Long id);

    @Query("SELECT DISTINCT MAX(o.createdDate) FROM Orders o WHERE o.user.id = ?1 AND o.showTiming.id = ?2")
    Date getLastOrderCreatedByUserAndShowTiming(Long userId, Long showTimingId);

    /*@Query("SELECT o.showTiming, COUNT(o.seat), o.ticketStatus, SUM(o.numberProducts), o.productsStatus, o.createdDate  " +
            "FROM Orders o " +
            "WHERE o.user.id = ?1 AND o.showTiming.id = ?2 AND o.createdDate = ?3")
    List<Orders> refreshOrdersStatus(Long userId, Long showTimingId, Date date);*/
}
