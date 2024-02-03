package com.hei.wallet.heiwallet.endpoint.rest.model;

import com.hei.wallet.heiwallet.model.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Transaction implements Serializable {
    private String id;
    private String label;
    private Instant transactionDatetime;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;

    public Transaction(
            String id,
            String label,
            Instant transactionDatetime,
            BigDecimal amount,
            TransactionType type,
            Category category
    ) {
        this.id = id;
        this.label = label;
        this.transactionDatetime = transactionDatetime;
        this.amount = amount;
        this.type = type;
        this.category = category;
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

    public Instant getTransactionDatetime() {
        return transactionDatetime;
    }

    public void setTransactionDatetime(Instant transactionDatetime) {
        this.transactionDatetime = transactionDatetime;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(label, that.label) && Objects.equals(transactionDatetime, that.transactionDatetime) && Objects.equals(amount, that.amount) && type == that.type && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, transactionDatetime, amount, type, category);
    }
}