package com.monetra.controllers;

import com.monetra.dto.AccountDetails;
import com.monetra.dto.TransactionReceipt;
import com.monetra.dto.request.DepositRequest;
import com.monetra.dto.response.TransactionReceiptResponse;
import com.monetra.services.AccountService;
import com.monetra.services.DepositService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor

public class AccountController {
    private final AccountService accountService;
    private final DepositService depositService;

    @GetMapping("/me")
    public AccountDetails getDashboardDetails(@AuthenticationPrincipal UserDetails userDetails){
        return accountService.getDashboardAccountDetails(userDetails);
    }

    @PostMapping("/deposit")
    public TransactionReceiptResponse deposit(
            @RequestBody DepositRequest depositRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("CONTROLLER HIT");
        System.out.println("USER DETAILS: " + userDetails);

        TransactionReceiptResponse transactionReceiptResponse = new TransactionReceiptResponse(
                true, "Successful Deposit", depositService.deposit(depositRequest, userDetails)
        );
        return transactionReceiptResponse;
    }
}
