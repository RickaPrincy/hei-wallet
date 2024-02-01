package com.hei.wallet.heiwallet.endpoint.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class CurrencyValue implements Serializable {
    private String id;
    private Instant effectiveDatetime;
    private BigDecimal amount;
    private Currency source;
    private Currency destination;

    public CurrencyValue(String id, Instant effectiveDatetime, BigDecimal amount, Currency source, Currency destination) {
        this.id = id;
        this.effectiveDatetime = effectiveDatetime;
        this.amount = amount;
        this.source = source;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getEffectiveDatetime() {
        return effectiveDatetime;
    }

    public void setEffectiveDatetime(Instant effectiveDatetime) {
        this.effectiveDatetime = effectiveDatetime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getSource() {
        return source;
    }

    public void setSource(Currency source) {
        this.source = source;
    }

    public Currency getDestination() {
        return destination;
    }

    public void setDestination(Currency destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyValue that = (CurrencyValue) o;
        return Objects.equals(id, that.id) && Objects.equals(effectiveDatetime, that.effectiveDatetime) && Objects.equals(amount, that.amount) && Objects.equals(source, that.source) && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, effectiveDatetime, amount, source, destination);
    }
}