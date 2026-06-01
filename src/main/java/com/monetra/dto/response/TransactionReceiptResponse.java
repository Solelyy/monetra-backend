package com.monetra.dto.response;

public record TransactionReceiptResponse<T> (
        boolean success,
        String message,
        T receipt
) {}