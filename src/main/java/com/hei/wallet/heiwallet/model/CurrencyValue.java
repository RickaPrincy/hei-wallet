package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity(tableName = "currency_value")
public class CurrencyValue implements Serializable {
    @Id
    @Column
    private String id;

    @Column(columnName = "effective_date")
    private Instant effectiveDatetime;

    @Column
    private BigDecimal amount;

    @Column
    @Relation
    private Currency source;

    @Column
    @Relation
    private Currency destination;

    public CurrencyValue(String id, Instant effectiveDatetime, BigDecimal amount, Currency source, Currency destination) {
        this.id = id;
        this.effectiveDatetime = effectiveDatetime;
        this.amount = amount;
        this.source = source;
        this.destination = destination;
    }

    public CurrencyValue() {
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
    public String toString() {
        return "CurrencyValue{" +
                "id='" + id + '\'' +
                ", effectiveDatetime=" + effectiveDatetime +
                ", amount=" + amount +
                ", source=" + source +
                ", destination=" + destination +
                '}';
    }
}
