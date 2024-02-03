package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.Column;
import com.hei.wallet.heiwallet.fjpa.annotation.Entity;
import com.hei.wallet.heiwallet.fjpa.annotation.Id;
import com.hei.wallet.heiwallet.fjpa.annotation.Relation;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "account")
public class Account implements Serializable {
    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private AccountType type;

    @Column
    @Relation
    private Currency currency;

    @Relation
    private List<Balance> balances;

    @Relation
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(
            String id,
            String name,
            AccountType type,
            Currency currency,
            List<Balance> balances,
            List<Transaction> transactions
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.currency = currency;
        this.balances = balances;
        this.transactions = transactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(name, account.name) && type == account.type && Objects.equals(currency, account.currency) && Objects.equals(balances, account.balances) && Objects.equals(transactions, account.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, currency, balances, transactions);
    }
}
