package com.monetra.services;

import com.monetra.common.TransactionHelper;
import com.monetra.dto.request.TransferRequest;
import com.monetra.dto.response.TransferReceipt;
import com.monetra.enums.TransactionType;
import com.monetra.exceptions.AccountNotFoundException;
import com.monetra.exceptions.InvalidAmountException;
import com.monetra.exceptions.InvalidRecipientException;
import com.monetra.models.Transaction;
import com.monetra.repositories.AccountRepository;
import com.monetra.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j

public class TransferService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransferReceipt transfer(TransferRequest transferRequest, UserDetails userDetails) {
        BigDecimal requestedAmount = transferRequest.getAmount();
        log.info("Transfer requested amount: {}", transferRequest.getAmount());

        String email = userDetails.getUsername();
        log.info("Email: {}", email);

        //1. check if the requested amount is null or negative
        if (requestedAmount == null || requestedAmount.signum() < 0) {
            throw new InvalidAmountException("Invalid amount");
        }

        BigDecimal min = BigDecimal.valueOf(50);
        BigDecimal max = BigDecimal.valueOf(50_000);

        //2. check if the requested amount is valid
        if (requestedAmount.compareTo(min) < 0 && requestedAmount.compareTo(max) > 0) {
            throw new InvalidAmountException("Amount must be ₱50 - ₱50,000");
        }

        //3. check if recipient is existing
        var recipientAccount = accountRepository.findByAccountNumber(transferRequest.getRecipientAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));

        //4. get sender account number and compare to the recipient
        var senderAccount = accountRepository.findByClientUserEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        if (senderAccount.getAccountNumber().equals(recipientAccount.getAccountNumber())) {
            throw new InvalidRecipientException("Cannot transfer funds to your own account.");
        }

        //5. check balance of the sender if exceeds the requested transfer amount
        if (senderAccount.getBalance().compareTo(requestedAmount) < 0) {
            throw new InvalidAmountException("Transfer amount exceeds available balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(requestedAmount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(requestedAmount));

        //6. create transaction for sender
        Transaction senderTransaction = Transaction.builder()
                .transactionNumber(TransactionHelper.generateTransactionNumber())
                .senderAccountNumber(senderAccount.getAccountNumber())
                .type(TransactionType.TRANSFER_OUT)
                .amount(requestedAmount)
                .receiverAccountNumber(transferRequest.getRecipientAccountNumber())
                .note(transferRequest.getNote())
                .account(senderAccount)
                .transferGroupId(TransactionHelper.generateTransferGroupId())
                .build();

        //7. create transaction for receiver

        Transaction recipientTransaction = Transaction.builder()
                .transactionNumber(TransactionHelper.generateTransactionNumber())
                .senderAccountNumber(senderTransaction.getSenderAccountNumber())
                .type(TransactionType.TRANSFER_IN)
                .amount(transferRequest.getAmount())
                .account(recipientAccount)
                .note(transferRequest.getNote())
                .receiverAccountNumber(transferRequest.getRecipientAccountNumber())
                .transferGroupId(senderTransaction.getTransferGroupId())
                .build();

        transactionRepository.save(senderTransaction);
        transactionRepository.save(recipientTransaction);

        //8. create transaction receipt
        TransferReceipt transferReceipt = TransactionHelper.createTransferReceipt(recipientTransaction, senderTransaction.getTransactionNumber());

        return transferReceipt;
    }
}
