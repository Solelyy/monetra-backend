package com.monetra.controllers;

import com.monetra.dto.AccountDetails;
import com.monetra.dto.request.TransactionRequest;
import com.monetra.dto.response.TransactionReceiptResponse;
import com.monetra.services.AccountService;
import com.monetra.services.DepositService;
import com.monetra.services.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor

public class AccountController {
    private final AccountService accountService;
    private final DepositService depositService;
    private final WithdrawService withdrawService;

    @GetMapping("/me")
    public AccountDetails getDashboardDetails(@AuthenticationPrincipal UserDetails userDetails){
        return accountService.getDashboardAccountDetails(userDetails);
    }

    @PostMapping("/deposit")
    public TransactionReceiptResponse deposit(
            @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        TransactionReceiptResponse transactionReceiptResponse = new TransactionReceiptResponse(
                true, "Successful Deposit!", depositService.deposit(transactionRequest, userDetails)
        );
        return transactionReceiptResponse;
    }

    @PostMapping("/withdraw")
    public TransactionReceiptResponse withdraw(
            @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        TransactionReceiptResponse transactionReceiptResponse = new TransactionReceiptResponse(
                true, "Successful withdrawal!", withdrawService.withdraw(transactionRequest, userDetails)
        );
        return  transactionReceiptResponse;
    }
}
