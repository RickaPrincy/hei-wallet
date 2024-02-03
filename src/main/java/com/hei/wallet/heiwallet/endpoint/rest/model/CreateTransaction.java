package com.hei.wallet.heiwallet.endpoint.rest.model;

import com.hei.wallet.heiwallet.model.TransactionType;

import java.math.BigDecimal;

public class CreateTransaction {
    private String id;
    private String label;
    private BigDecimal amount;
    private TransactionType type;
    private String categoryId;
    private String accountId;

    public CreateTransaction(
            String id,
            String label,
            BigDecimal amount,
            TransactionType type,
            String categoryId,
            String accountId
    ) {
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.type = type;
        this.categoryId = categoryId;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}