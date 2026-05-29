package com.monetra.transaction.repository;

import com.monetra.account.entity.Account;
import com.monetra.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByAccountOrderByCreatedAtDesc(Account account);

}
