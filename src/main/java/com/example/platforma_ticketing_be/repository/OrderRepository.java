package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {

    @Query("SELECT st, COUNT(o.showTiming.id), o.ticketStatus, SUM(o.numberProducts), o.productsStatus  " +
            "FROM ShowTiming st " +
            "INNER JOIN Orders o ON o.showTiming.id = st.id " +
            "WHERE o.user.id = :id " +
            "GROUP BY st, o.ticketStatus, o.productsStatus " +
            "ORDER BY st.day desc, st.time desc")
    List<Object[]> findOrdersOfAUser(Pageable pageable, Long id);

    @Query("SELECT st, COUNT(o.showTiming.id), o.ticketStatus, SUM(o.numberProducts), o.productsStatus  " +
            "FROM ShowTiming st " +
            "INNER JOIN Orders o ON o.showTiming.id = st.id " +
            "WHERE o.user.id = :id and st.id in :ids " +
            "GROUP BY st, o.ticketStatus, o.productsStatus " +
            "ORDER BY st.day desc, st.time desc")
    List<Object[]> findFilteredOrdersOfAUser(Pageable pageable, Long id, Set<Long> ids);

    List<Orders> findOrdersByUserIdAndShowTimingId(Long userId, Long showTimingId);

    @Query("select o.seat from Orders o where o.showTiming.id = ?1")
    Set<String> findSeatsByShowTimingId(Long id);
}
