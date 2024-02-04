package com.hei.wallet.heiwallet.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class CategorySum implements Serializable {
    String category;
    BigDecimal amount;

    public CategorySum(String category, BigDecimal amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        CategorySum that = (CategorySum) o;
        return Objects.equals(category, that.category) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, amount);
    }
}