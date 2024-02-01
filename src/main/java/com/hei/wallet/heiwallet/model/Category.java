package com.hei.wallet.heiwallet.model;

import com.hei.wallet.heiwallet.fjpa.annotation.Column;
import com.hei.wallet.heiwallet.fjpa.annotation.Entity;
import com.hei.wallet.heiwallet.fjpa.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Category implements Serializable {
    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private CategoryType type;

    public Category(String id, String name, CategoryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Category() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && type == category.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}