package com.monetra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionReceipt (
        BigDecimal amount,
        String referenceNumber,
        LocalDateTime timestamp,
        String recipientAccountNumber,
        String note
) {}