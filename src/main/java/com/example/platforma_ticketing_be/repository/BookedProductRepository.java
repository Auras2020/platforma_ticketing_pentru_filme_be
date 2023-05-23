/*
package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.BookedProduct;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookedProductRepository extends JpaRepository<BookedProduct, Long>, JpaSpecificationExecutor<BookedProduct> {

    @Query("select b from BookedProduct b where b.user = ?1 order by b.showTiming.day, b.showTiming.time desc")
    List<BookedProduct> findBookedProductByUserIdOrderedByDayAndTimeDesc(UserAccount userAccount);

    @Query(value = "SELECT st.id, s1.count_number, s1.ticket_status, bp1.sum_number, bp1.products_status " +
            "FROM show_timing st " +
            "INNER JOIN (SELECT s.show_timing_id, COUNT(s.id) AS count_number, s.status AS ticket_status " +
            "            FROM seat s " +
            "            WHERE s.user_id = :id " +
            "            GROUP BY s.show_timing_id, s.status) s1 ON st.id = s1.show_timing_id " +
            "LEFT JOIN (SELECT bp.show_timing_id, SUM(bp.number) AS sum_number, bp.status AS products_status " +
            "            FROM booked_product bp " +
            "            WHERE bp.user_id = :id " +
            "            GROUP BY bp.show_timing_id,  bp.status) bp1 ON st.id = bp1.show_timing_id " +
            "            ORDER BY st.day DESC, st.time DESC" ,
            nativeQuery = true)
    List<Object[]> findBookProductsOfAUser(Pageable pageable, Long id);

//, COUNT(s.showTiming.id), s.status
    // and s.user.id = :id
    // , s.status

    */
/*@Query("SELECT st, SUM(bp.number), bp.status " +
            "FROM ShowTiming st " +
            "INNER JOIN BookedProduct bp ON bp.showTiming.id = st.id " +
           *//*
*/
/* "INNER JOIN Seat s ON s.showTiming.id = bp.showTiming.id " +*//*
*/
/*
            "WHERE bp.user.id = :id and st.id in :ids " +
            "GROUP BY st, bp.status " +
            "ORDER BY st.day desc, st.time desc")*//*

   */
/* @Query(value = "SELECT st.id, s.count_number, s.status, bp.sum_number " +
            "FROM show_timing st " +
            "LEFT JOIN (SELECT s.show_timing_id, COUNT(s.id) as count_number, s.status " +
            "           FROM seat s " +
            "           WHERE s.user.id = :id AND st.id IN :ids " +
            "           GROUP BY st.id, s.status) s ON st.id = s.show_timing_id " +
            "LEFT JOIN (SELECT s.show_timing_id, SUM(bp.number) as sum_number " +
            "           FROM booked_product bp " +
            "           WHERE bp.user_id = :id AND st.id IN :ids " +
            "           GROUP BY st.id) bp ON st.id = bp.show_timing_id " +
            "ORDER BY st.day DESC, st.time DESC",
            nativeQuery = true)*//*

    @Query(value = "SELECT st.id, s1.count_number, s1.ticket_status, bp1.sum_number, bp1.products_status " +
            "FROM show_timing st " +
            "INNER JOIN (SELECT s.show_timing_id, COUNT(s.id) AS count_number, s.status AS ticket_status " +
            "            FROM seat s " +
            "            WHERE s.user_id = :id " +
            "            GROUP BY s.show_timing_id, s.status) s1 ON st.id = s1.show_timing_id " +
            "LEFT JOIN (SELECT bp.show_timing_id, SUM(bp.number) AS sum_number, bp.status AS products_status " +
            "            FROM booked_product bp " +
            "            WHERE bp.user_id = :id " +
            "            GROUP BY bp.show_timing_id, bp.status) bp1 ON st.id = bp1.show_timing_id " +
            "WHERE st.id IN :ids " +
            "ORDER BY st.day DESC, st.time DESC",
            nativeQuery = true)
    List<Object[]> findFilteredBookProductsOfAUser(Pageable pageable, Long id, Set<Long> ids);


    List<BookedProduct> findBookedProductByUserIdAndShowTimingId(Long userId, Long showTimingId);
}
*/
