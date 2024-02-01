package com.hei.wallet.heiwallet.fjpa;

import java.util.Objects;

public class Attribute {
    private String columnName;
    private String fieldName;
    private boolean required;
    private Class<?> fieldType;

    public Attribute(String columnName, String fieldName, boolean required, Class<?> fieldType) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.required = required;
        this.fieldType = fieldType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return required == attribute.required && Objects.equals(columnName, attribute.columnName) && Objects.equals(fieldName, attribute.fieldName) && Objects.equals(fieldType, attribute.fieldType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, fieldName, required, fieldType);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "columnName='" + columnName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", required=" + required +
                ", fieldType=" + fieldType +
                '}';
    }
}