package com.monetra.transaction.dto;

import com.monetra.transaction.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String note;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private LocalDateTime timestamp;
}
