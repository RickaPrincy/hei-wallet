package com.hei.wallet.heiwallet.endpoint.rest.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class CreateCurrencyValue implements Serializable {
    private String id;
    private Instant effectiveDatetime;
    private BigDecimal amount;
    private String sourceId;
    private String destinationId;

    public CreateCurrencyValue(String id, Instant effectiveDatetime, BigDecimal amount, String sourceId, String destinationId) {
        this.id = id;
        this.effectiveDatetime = effectiveDatetime;
        this.amount = amount;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateCurrencyValue that = (CreateCurrencyValue) o;
        return Objects.equals(id, that.id) && Objects.equals(effectiveDatetime, that.effectiveDatetime) && Objects.equals(amount, that.amount) && Objects.equals(sourceId, that.sourceId) && Objects.equals(destinationId, that.destinationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, effectiveDatetime, amount, sourceId, destinationId);
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }
}
