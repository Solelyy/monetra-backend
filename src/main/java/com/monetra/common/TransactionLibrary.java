package com.monetra.common;

import com.monetra.dto.TransactionReceipt;
import com.monetra.models.Transaction;

import java.security.SecureRandom;

public class TransactionLibrary {
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
}
