package com.banking.controller;

import com.banking.model.Account;
import com.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/accounts", "/accounts"})
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            if (account.getAccountNumber() == null || account.getAccountNumber().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account number is required.");
            }
            if (account.getOwnerName() == null || account.getOwnerName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Owner name is required.");
            }
            Account created = accountService.createAccount(account);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
