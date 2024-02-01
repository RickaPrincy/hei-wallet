package com.hei.wallet.heiwallet.fjpa;

import com.hei.wallet.heiwallet.fjpa.annotation.Column;
import com.hei.wallet.heiwallet.fjpa.annotation.Entity;
import com.hei.wallet.heiwallet.fjpa.annotation.Id;
import com.hei.wallet.heiwallet.fjpa.annotation.Relation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Used to get metadata about the generic class
 * @param <T>
 */
public class ReflectModel<T>{
    private final Class<T> type;
    private final String tableName;
    private final List<Attribute> attributes;
    private Attribute idAttribute;

    public ReflectModel(Class<T> type) {
        this.type = type;
        this.tableName = getReflectedTableName();
        this.attributes = getReflectedAttributes();
    }

    public String joinAttributesNames(String limiter) {
        return getAttributes()
                .stream()
                .map(Attribute::getColumnName)
                .collect(Collectors.joining(limiter));
    }

    public String joinAttributesNamesWithoutId(String limiter){
        return getAttributes()
                .stream()
                .filter(el -> !idAttribute.equals(el))
                .map(Attribute::getColumnName)
                .collect(Collectors.joining(limiter));
    }

    protected Object getAttributeValue(T object, Attribute attribute) {
        String columnName = attribute.getFieldName();
        try {
            String methodName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            Method method = object.getClass().getMethod(methodName);
            return method.invoke(object);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Getter error for " + columnName);
        }
    }

    protected T mapResultSetToInstance(ResultSet resultSet) {
        Map<String, Object> values = new HashMap<>();
        for(Attribute attribute: getAttributes()){
            if(attribute.isRelation())
                continue;
            try {
                Object value;
                if(Instant.class.equals(attribute.getFieldType())){
                    value = resultSet.getTimestamp(attribute.getColumnName()).toInstant();
                }else if(BigDecimal.class.equals(attribute.getFieldType())){
                    value = resultSet.getBigDecimal(attribute.getColumnName());
                } else if (attribute.getFieldType().isEnum()) {
                    String enumString = resultSet.getString(attribute.getColumnName());
                    value = Enum.valueOf((Class<Enum>) attribute.getFieldType(), enumString);
                } else {
                    value = resultSet.getObject(attribute.getColumnName());
                }
                values.put(attribute.getFieldName(), value);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        };

        return createInstance(values);
    }

    private T createInstance(Map<String, Object> argsValues) {
        Constructor<T> constructor = Arrays.stream((Constructor<T>[]) type.getDeclaredConstructors())
                .filter(el -> el.getParameterCount() == 0)
                .findFirst().orElseThrow(RuntimeException::new);
        try{
            T newInstance = constructor.newInstance();
            newInstance = setFields(newInstance, argsValues);
            return newInstance;
        }catch(InvocationTargetException | InstantiationException | IllegalAccessException error){
            throw new RuntimeException("Instantiation error for " + getTableName());
        }
    }

    private T setFields(T instance, Map<String, Object> argsValues) {
        argsValues.forEach((fieldName, value) -> {
            try {
                String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method setterMethod = Arrays.stream(type.getMethods())
                        .filter(method -> method.getName().equals(setterMethodName))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchMethodException("Setter method not found for field: " + fieldName));
                setterMethod.invoke(instance, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error setting field " + fieldName + " for " + type.getSimpleName(), e);
            }
        });
        return instance;
    }

    private String getReflectedTableName(){
        if(!type.isAnnotationPresent(Entity.class))
            throw new RuntimeException("Class must be annotated with @Entity to be used with fjpa");
        Entity entity = type.getAnnotation(Entity.class);
        return entity.tableName().isEmpty() ? type.getSimpleName().toLowerCase() : entity.tableName();
    }

    private List<Attribute> getReflectedAttributes(){
        List<Attribute> attributes = new ArrayList<>();
        Field[] fields = Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .toArray(Field[]::new);

        Arrays.stream(fields).forEach(field -> {
            Column clAnnotation = field.getAnnotation(Column.class);
            Attribute attribute = new Attribute(
                    clAnnotation.columnName().isEmpty() ? field.getName().toLowerCase() : clAnnotation.columnName(),
                    field.getName(),
                    clAnnotation.required(),
                    field.getType(),
                    field.isAnnotationPresent(Relation.class)
            );

            if(field.isAnnotationPresent(Id.class)){
                idAttribute = attribute;
            }
            attributes.add(attribute);
        });
        return attributes;
    }


    public Class<T> getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Attribute getIdAttribute() {
        return idAttribute;
    }
}