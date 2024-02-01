package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Currency implements Serializable {
    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private String code;

    private List<CurrencyValue> currencyValues = new ArrayList<>();

    public Currency() {
    }

    public Currency(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public List<CurrencyValue> getCurrencyValues() {
        return currencyValues;
    }

    public void setCurrencyValues(List<CurrencyValue> currencyValues) {
        this.currencyValues = currencyValues;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id) && Objects.equals(name, currency.name) && Objects.equals(code, currency.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}