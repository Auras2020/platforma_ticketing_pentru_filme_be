package com.example.platforma_ticketing_be.repository;

import com.example.platforma_ticketing_be.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {

    UserAccount findByEmailAndPassword(String email, String password);

    UserAccount findByEmail(String email);
}
