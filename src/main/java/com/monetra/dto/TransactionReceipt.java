package com.monetra.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionReceipt (
        BigDecimal amount,
        String referenceNumber,
        LocalDateTime timestamp,
        String recipientAccountNumber,
        String note
) {}