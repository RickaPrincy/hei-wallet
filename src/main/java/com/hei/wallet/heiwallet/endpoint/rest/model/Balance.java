package com.hei.wallet.heiwallet.endpoint.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Balance implements Serializable {
    private String id;
    private Instant updatedAt;
    private BigDecimal amount;

    public Balance(String id, Instant updatedAt, BigDecimal amount) {
        this.id = id;
        this.updatedAt = updatedAt;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return Objects.equals(id, balance.id) && Objects.equals(updatedAt, balance.updatedAt) && Objects.equals(amount, balance.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, updatedAt, amount);
    }
}
