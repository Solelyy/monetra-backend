package com.monetra.services;

import com.monetra.dto.AccountDetails;
import com.monetra.models.Account;
import com.monetra.repositories.AccountRepository;
import com.monetra.dto.TransactionResponse;
import com.monetra.models.Transaction;
import com.monetra.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountDetails getDashboardAccountDetails (UserDetails userDetails) {
        Account account = accountRepository
                .findByClientUserEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        List<Transaction> transactions =
                transactionRepository.findTop5ByAccountOrderByCreatedAtDesc(account);

        List<TransactionResponse> recentTransactions =
                transactions.stream()
                        .map(this::mapToTransactionResponse)
                        .toList();
        return AccountDetails.builder()
                .accountNumber(account.getAccountNumber())
                .accountBalance(account.getBalance())
                .accountName(account.getClient().getFirstName() + " " +
                        account.getClient().getLastName())
                .recentTransactions(recentTransactions)
                .build();
    }

    private TransactionResponse mapToTransactionResponse (Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .note(transaction.getNote())
                .type(transaction.getType())
                .senderAccountNumber(transaction.getSenderAccountNumber())
                .receiverAccountNumber(transaction.getReceiverAccountNumber())
                .timestamp(transaction.getCreatedAt())
                .build();
    }
}
