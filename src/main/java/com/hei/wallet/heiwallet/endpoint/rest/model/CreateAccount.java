package com.hei.wallet.heiwallet.endpoint.rest.model;

import com.hei.wallet.heiwallet.model.AccountType;

import java.util.Objects;

public class CreateAccount{
    private String id;
    private String name;
    private AccountType type;

    private String currencyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAccount that = (CreateAccount) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type && Objects.equals(currencyId, that.currencyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, currencyId);
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

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public CreateAccount(String id, String name, AccountType type, String currencyId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.currencyId = currencyId;
    }
}