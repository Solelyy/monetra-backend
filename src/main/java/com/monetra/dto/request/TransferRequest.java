package com.monetra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TransferRequest {
    private BigDecimal amount;
    private String note;
    private String recipientAccountNumber;
}
