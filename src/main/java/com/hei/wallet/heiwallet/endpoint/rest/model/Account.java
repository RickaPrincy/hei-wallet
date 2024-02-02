package com.hei.wallet.heiwallet.endpoint.rest.model;

import com.hei.wallet.heiwallet.model.AccountType;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    private String id;
    private String name;
    private AccountType type;
    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(name, account.name) && type == account.type && Objects.equals(currency, account.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, currency);
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

    public Account(String id, String name, AccountType type, Currency currency) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.currency = currency;
    }
}