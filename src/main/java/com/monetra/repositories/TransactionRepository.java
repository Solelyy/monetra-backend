package com.monetra.repositories;

import com.monetra.models.Account;
import com.monetra.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByAccountOrderByCreatedAtDesc(Account account);

}
