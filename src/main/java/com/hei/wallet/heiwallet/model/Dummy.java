package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.Column;
import com.hei.wallet.heiwallet.fjpa.annotation.Entity;
import com.hei.wallet.heiwallet.fjpa.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "dummy")
public class Dummy implements Serializable {
    @Id
    @Column
    private String id;

    @Column
    private String name;

    public Dummy(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dummy() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dummy dummy = (Dummy) o;
        return Objects.equals(id, dummy.id) && Objects.equals(name, dummy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Dummy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
