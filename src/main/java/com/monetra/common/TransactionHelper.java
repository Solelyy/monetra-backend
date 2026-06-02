package com.monetra.common;

import com.monetra.dto.response.TransactionReceipt;
import com.monetra.dto.response.TransferReceipt;
import com.monetra.models.Transaction;

import java.security.SecureRandom;
import java.util.UUID;

public class TransactionHelper {
    private static final SecureRandom random = new SecureRandom();

    public static String generateTransactionNumber() {
        long timePart = System.currentTimeMillis();
        int randomPart = random.nextInt(1_000_000);

        String base36 = Long.toString(timePart, 36)
                + Integer.toString(randomPart, 36);

        return trimTransNum(base36.toUpperCase());
    }

    private static String trimTransNum(String value) {
        if (value.length() > 12) {
            return value.substring(value.length() - 12);
        }

        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < 12) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    public static TransactionReceipt createReceipt(Transaction transaction) {
        return new TransactionReceipt(
                transaction.getAmount(),
                transaction.getTransactionNumber(),
                transaction.getCreatedAt(),
                transaction.getReceiverAccountNumber(),
                transaction.getNote()
        );
    }

    public static TransferReceipt createTransferReceipt(Transaction transaction, String transactionNumber) {
        String firstName = transaction.getAccount().getClient().getFirstName();
        String lastName = transaction.getAccount().getClient().getLastName();
        String fullName = firstName + " " + lastName;

        return new TransferReceipt(
                transaction.getAmount(),
                transactionNumber,
                transaction.getCreatedAt(),
                transaction.getReceiverAccountNumber(),
                fullName,
                transaction.getNote()
        );
    }

    public static String generateTransferGroupId() {
        return "TRF-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }

}
