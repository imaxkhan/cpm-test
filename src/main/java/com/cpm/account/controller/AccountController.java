package com.cpm.account.controller;

import com.cpm.account.dto.account.AccountBalanceResponse;
import com.cpm.account.dto.account.AccountRequest;
import com.cpm.account.dto.account.AccountResponse;
import com.cpm.account.dto.account.AccountTransferRequest;
import com.cpm.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @PostMapping("/init")
    public ResponseEntity<AccountResponse> create(@RequestBody @Valid AccountRequest request) {
        return new ResponseEntity<>(service.initAccount(request), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody @Valid AccountTransferRequest request) {
        service.transferMoney(request);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<AccountBalanceResponse> getBalance(@PathVariable Long id) {
        return new ResponseEntity<>(service.getBalance(id), HttpStatus.OK);
    }
}
