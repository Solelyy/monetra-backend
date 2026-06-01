package com.monetra.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferReceipt (
        BigDecimal amount,
        String referenceNumber,
        LocalDateTime timestamp,
        String recipientAccountNumber,
        String recipientAccountName,
        String note
) {}