package com.monetra.services;

import com.monetra.common.TransactionLibrary;
import com.monetra.dto.TransactionReceipt;
import com.monetra.dto.request.DepositRequest;
import com.monetra.enums.TransactionType;
import com.monetra.exceptions.AccountNotFoundException;
import com.monetra.exceptions.InvalidAmountException;
import com.monetra.models.Transaction;
import com.monetra.repositories.AccountRepository;
import com.monetra.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j

public class DepositService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionReceipt deposit(DepositRequest depositRequest, UserDetails userDetails) {
        log.info("Deposit request: {}", depositRequest.getAmount());

//      String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String email = userDetails.getUsername();

        log.info("Email: {}", email);
        BigDecimal requestedAmount = depositRequest.getAmount();

        if (requestedAmount == null || requestedAmount.signum() <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }

        BigDecimal min = BigDecimal.valueOf(50);
        BigDecimal max = BigDecimal.valueOf(50_000);

        if (requestedAmount.compareTo(min) < 0 ||
                requestedAmount.compareTo(max) > 0) {
            throw new InvalidAmountException("Deposit must be ₱50 - ₱50,000");
        }

        //1. check the account and add the amount requested
        var account = accountRepository.findByClientUserEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        log.info("Account found: {}", account);

        account.setBalance(account.getBalance().add(requestedAmount));

        //2. create transaction
        Transaction transaction = Transaction.builder()
                .transactionNumber(TransactionLibrary.generateTransactionNumber())
                .account(account)
                .amount(requestedAmount)
                .type(TransactionType.DEPOSIT)
                .senderAccountNumber(account.getAccountNumber())
                .receiverAccountNumber(account.getAccountNumber())
                .build();
        transactionRepository.save(transaction);

        //3. create TransactionReceipt
        TransactionReceipt transactionReceipt = TransactionLibrary.createReceipt(transaction);

        return transactionReceipt;
    }
}
