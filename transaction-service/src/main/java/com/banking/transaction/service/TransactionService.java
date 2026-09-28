package com.banking.transaction.service;

import com.banking.transaction.dto.AccountDTO;
import com.banking.transaction.dto.TransferRequest;
import com.banking.transaction.model.Transaction;
import com.banking.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    @Value("${account.service.url:http://account-service:8080}")
    private String accountServiceUrl;

    public TransactionService(TransactionRepository transactionRepository, RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    public AccountDTO checkAccountExists(String accountNumber) {
        String url = accountServiceUrl + "/api/accounts/number/" + accountNumber;
        try {
            ResponseEntity<AccountDTO> response = restTemplate.getForEntity(url, AccountDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with account-service via internal DNS (" + url + "): " + e.getMessage(), e);
        }
        return null;
    }

    public Transaction processTransfer(TransferRequest request) {
        // Step 1: Verify source account exists via internal HTTP call (DNS: http://account-service:8080)
        AccountDTO sourceAccount = checkAccountExists(request.getSourceAccountNumber());
        if (sourceAccount == null) {
            throw new IllegalArgumentException("Source account does not exist: " + request.getSourceAccountNumber());
        }

        // Step 2: Verify target account exists via internal HTTP call (DNS: http://account-service:8080)
        AccountDTO targetAccount = checkAccountExists(request.getTargetAccountNumber());
        if (targetAccount == null) {
            throw new IllegalArgumentException("Target account does not exist: " + request.getTargetAccountNumber());
        }

        // Step 3: Record transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transaction.setSourceAccount(request.getSourceAccountNumber());
        transaction.setTargetAccount(request.getTargetAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
