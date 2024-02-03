package com.hei.wallet.heiwallet.endpoint.rest.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CreateTransfer {
    private String id;
    private String sourceId;
    private String destinationId;
    private BigDecimal amount;

    public CreateTransfer(String id, String sourceId, String destinationId, BigDecimal amount) {
        this.id = id;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
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
        CreateTransfer that = (CreateTransfer) o;
        return Objects.equals(id, that.id) && Objects.equals(sourceId, that.sourceId) && Objects.equals(destinationId, that.destinationId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceId, destinationId, amount);
    }
}
