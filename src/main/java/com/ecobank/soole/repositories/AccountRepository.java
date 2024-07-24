package com.ecobank.soole.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecobank.soole.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    List<Account> findByVerified(String status);
    List<Account> findByAuthorities(String authority);
    Optional<Account> findByToken(String token);

     @Query("SELECT a FROM Account a WHERE " +
       "(:name IS NULL OR a.firstName LIKE %:name% OR a.lastName LIKE %:name%) " +
       "AND (:authorities IS NULL OR a.authorities LIKE %:authorities%)")
Page<Account> findByNameAndDate(
    @Param("name") String name,
    @Param("authorities") String authorities,
    Pageable pageable
);
}
