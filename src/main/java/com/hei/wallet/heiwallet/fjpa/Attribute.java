package com.hei.wallet.heiwallet.fjpa;

import java.util.Objects;

public class Attribute {
    private String columnName;
    private String fieldName;
    private boolean required;
    private Class<?> fieldType;
    private boolean isRelation;

    public Attribute(String columnName, String fieldName, boolean required, Class<?> fieldType, boolean isRelation) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.required = required;
        this.fieldType = fieldType;
        this.isRelation = isRelation;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isRequired() {
        return required;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isRelation() {
        return isRelation;
    }

    public void setRelation(boolean relation) {
        isRelation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return required == attribute.required && isRelation == attribute.isRelation && Objects.equals(columnName, attribute.columnName) && Objects.equals(fieldName, attribute.fieldName) && Objects.equals(fieldType, attribute.fieldType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, fieldName, required, fieldType, isRelation);
    }
}