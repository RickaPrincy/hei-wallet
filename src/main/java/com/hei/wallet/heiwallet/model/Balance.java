package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.Column;
import com.hei.wallet.heiwallet.fjpa.annotation.Entity;
import com.hei.wallet.heiwallet.fjpa.annotation.Id;
import com.hei.wallet.heiwallet.fjpa.annotation.Relation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity(tableName = "balance_history")
public class Balance implements Serializable {
    @Id
    @Column
    private String id;

    @Column(columnName = "balance")
    private BigDecimal amount;

    @Column(columnName = "creation_datetime")
    private Instant creationDatetime;

    @Column
    @Relation
    private Account account;

    public Balance(String id, BigDecimal amount, Instant creationDatetime, Account account) {
        this.id = id;
        this.amount = amount;
        this.creationDatetime = creationDatetime;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return Objects.equals(id, balance.id) && Objects.equals(amount, balance.amount) && Objects.equals(creationDatetime, balance.creationDatetime) && Objects.equals(account, balance.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, creationDatetime, account);
    }

    public Balance() {
    }
}