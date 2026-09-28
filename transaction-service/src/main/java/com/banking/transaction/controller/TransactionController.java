package com.banking.transaction.controller;

import com.banking.transaction.dto.TransferRequest;
import com.banking.transaction.model.Transaction;
import com.banking.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/transactions", "/transactions"})
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/health")
    public String health() {
        return "Transaction Service is running and connected via Docker DNS!";
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            if (request.getSourceAccountNumber() == null || request.getSourceAccountNumber().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source account number is required.");
            }
            if (request.getTargetAccountNumber() == null || request.getTargetAccountNumber().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Target account number is required.");
            }
            if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transfer amount must be greater than zero.");
            }

            Transaction transaction = transactionService.processTransfer(request);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransferRequest request) {
        return transfer(request);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}
