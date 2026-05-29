package com.monetra.account.controller;

import com.monetra.account.dto.AccountDetails;
import com.monetra.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor

public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public AccountDetails getDashboardDetails(@AuthenticationPrincipal UserDetails userDetails){

        return accountService.getDashboardAccountDetails(userDetails);
    }
}
