package com.monetra.dto;

public record TransactionReceiptResponse (
        boolean success,
        String message,
        TransactionReceipt receipt
) {}