package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {

    UserAccount findByEmailAndPassword(String email, String password);

    UserAccount findByEmail(String email);

    @Query("select u from UserAccount u where u.pending = false")
    List<UserAccount> getAllActiveAccounts(Pageable pageable);

    @Query("select u from UserAccount u where u.pending = false and u.id in :ids")
    List<UserAccount> getAllFilteredActiveAccounts(Pageable pageable, Set<Long> ids);

    @Query("select u from UserAccount u where u.pending = true")
    List<UserAccount> getAllPendingAccounts(Pageable pageable);

    @Query("select u from UserAccount u where u.pending = true and u.id in :ids")
    List<UserAccount> getAllFilteredPendingAccounts(Pageable pageable, Set<Long> ids);

    @Query("select count(u.id) from UserAccount u where u.pending = true")
    int findNumberOfPendingRegistrations();
}
