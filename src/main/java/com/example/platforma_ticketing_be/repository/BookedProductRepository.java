package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.BookedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedProductRepository extends JpaRepository<BookedProduct, Long>, JpaSpecificationExecutor<BookedProduct> {

    @Query("select b from BookedProduct b where b.user.id = ?1 order by b.showTiming.day, b.showTiming.time desc")
    List<BookedProduct> findBookedProductByUserIdOrderedByDayAndTimeDesc(Long userId);
}
