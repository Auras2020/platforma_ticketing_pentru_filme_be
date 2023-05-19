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

    @Query("SELECT st, SUM(bp.number), bp.status " +
            "FROM ShowTiming st " +
            "INNER JOIN BookedProduct bp ON bp.showTiming.id = st.id " +
            "WHERE bp.user.id = :id " +
            "GROUP BY st, bp.status " +
            "ORDER BY st.day desc, st.time desc")
    List<Object[]> findBookProductsOfAUser(Pageable pageable, Long id);

    @Query("SELECT st, SUM(bp.number), bp.status " +
            "FROM ShowTiming st " +
            "INNER JOIN BookedProduct bp ON bp.showTiming.id = st.id " +
            "WHERE bp.user.id = :id and st.id in :ids " +
            "GROUP BY st, bp.status " +
            "ORDER BY st.day desc, st.time desc")
    List<Object[]> findFilteredBookProductsOfAUser(Pageable pageable, Long id, Set<Long> ids);

    List<BookedProduct> findBookedProductByShowTimingId(Long showTimingId);
}
