package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.Column;
import com.hei.wallet.heiwallet.fjpa.annotation.Entity;
import com.hei.wallet.heiwallet.fjpa.annotation.Id;
import com.hei.wallet.heiwallet.fjpa.annotation.Relation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity(tableName = "transfer")
public class Transfer implements Serializable {
    @Id
    @Column
    private String id;

    @Column
    private BigDecimal amount;

    @Column(columnName = "creation_datetime")
    private Instant creationDatetime;

    @Column
    @Relation
    private Account source;

    @Column
    @Relation
    private Account destination;

    public Transfer(String id, BigDecimal amount, Instant creationDatetime, Account source, Account destination) {
        this.id = id;
        this.amount = amount;
        this.creationDatetime = creationDatetime;
        this.source = source;
        this.destination = destination;
    }

    public Transfer() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(id, transfer.id) && Objects.equals(amount, transfer.amount) && Objects.equals(creationDatetime, transfer.creationDatetime) && Objects.equals(source, transfer.source) && Objects.equals(destination, transfer.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, creationDatetime, source, destination);
    }
}
