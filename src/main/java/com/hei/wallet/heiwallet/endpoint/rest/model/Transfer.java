package com.hei.wallet.heiwallet.endpoint.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class Transfer implements Serializable {
    private String id;
    private BigDecimal amount;
    private Instant creationDatetime;
    private Account source;
    private Account destination;

    public Transfer(String id, BigDecimal amount, Instant creationDatetime, Account source, Account destination) {
        this.id = id;
        this.amount = amount;
        this.creationDatetime = creationDatetime;
        this.source = source;
        this.destination = destination;
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

    public Account getSource() {
        return source;
    }

    public void setSource(Account source) {
        this.source = source;
    }

    public Account getDestination() {
        return destination;
    }

    public void setDestination(Account destination) {
        this.destination = destination;
    }
}