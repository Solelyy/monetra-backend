package com.monetra.controllers;

import com.monetra.common.ApiResponse;
import com.monetra.dto.request.TransferRequest;
import com.monetra.dto.response.AccountDetails;
import com.monetra.dto.request.TransactionRequest;
import com.monetra.dto.response.TransactionReceipt;
import com.monetra.dto.response.TransferReceipt;
import com.monetra.services.AccountService;
import com.monetra.services.DepositService;
import com.monetra.services.TransferService;
import com.monetra.services.WithdrawService;
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
    private final WithdrawService withdrawService;
    private final TransferService transferService;

    @GetMapping("/me")
    public AccountDetails getDashboardDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return accountService.getDashboardAccountDetails(userDetails);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionReceipt>> deposit(
            @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Deposit successful",
                        depositService.deposit(transactionRequest, userDetails)
                )
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionReceipt>> withdraw(
            @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Withdrawal successful",
                        withdrawService.withdraw(transactionRequest, userDetails)
                )
        );
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferReceipt>> transfer(
            @RequestBody TransferRequest transferRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Transfer successful",
                        transferService.transfer(transferRequest, userDetails)
                )
        );
    }
}
