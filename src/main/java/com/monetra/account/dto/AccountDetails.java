package com.monetra.account.dto;

import com.monetra.transaction.dto.TransactionResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AccountDetails {
    private String accountNumber;
    private BigDecimal accountBalance;
    private String accountName;
    private List<TransactionResponse> recentTransactions;
}
