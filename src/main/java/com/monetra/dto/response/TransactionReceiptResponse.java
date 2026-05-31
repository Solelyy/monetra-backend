package com.monetra.dto.response;

import com.monetra.dto.TransactionReceipt;

public record TransactionReceiptResponse (
        boolean success,
        String message,
        TransactionReceipt receipt
) {}