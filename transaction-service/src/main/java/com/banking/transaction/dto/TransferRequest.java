package com.banking.transaction.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
    private String description;

    public TransferRequest() {
    }

    public TransferRequest(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, String description) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
        this.description = description;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
