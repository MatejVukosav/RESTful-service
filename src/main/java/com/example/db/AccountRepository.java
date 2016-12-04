package com.example.db;


import com.example.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by mvukosav on 4.12.2016..
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

//    Optional<Account> findById(Long id);
}
