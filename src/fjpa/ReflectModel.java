package fjpa;

import fjpa.annotation.Column;
import fjpa.annotation.Entity;
import fjpa.annotation.Id;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Used to get metadata about the generic class
 * @param <T>
 */
public class ReflectModel<T>{
    protected Class<T> type;
    protected Attribute idAttribute;

    @Getter
    private final String tableName;

    @Getter
    private final List<Attribute> attributes;

    public ReflectModel(Class<T> type) {
        this.type = type;
        this.tableName = getReflectedTableName();
        this.attributes = geTtReflectedAttributes();
    }

    public T createInstance(@NonNull Map<String, Object> argsValues) {
        List<Constructor<T>> constructors =
            Arrays.stream((Constructor<T>[]) type.getDeclaredConstructors())
                .filter(el -> el.getParameterCount() == 0)
                .toList();
        if(constructors.isEmpty())
            throw new RuntimeException("Entity must have no args constructor");
        try{
            T newInstance = constructors.get(0).newInstance();
            newInstance = setFields(newInstance, argsValues);
            return newInstance;
        }catch(InvocationTargetException | InstantiationException | IllegalAccessException error){
            throw new RuntimeException("Instantiation error for " + getTableName());
        }
    }

    public T setFields(T instance, Map<String, Object> argsValues) {
        for (Map.Entry<String, Object> entry : argsValues.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

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
        }
        return instance;
    }

     private String getReflectedTableName(){
        if(!type.isAnnotationPresent(Entity.class))
            throw new RuntimeException("Class must be annotated with @Entity to be used with fjpa");
        Entity entity = type.getAnnotation(Entity.class);
        return entity.tableName().isEmpty() ? type.getSimpleName().toLowerCase() : entity.tableName();
    }

    private List<Attribute> geTtReflectedAttributes(){
        List<Attribute> attributes = new ArrayList<>();
        Field[] fields = Arrays.stream(type.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class))
            .toArray(Field[]::new);

        for(Field field: fields){
            Column clAnnotation = field.getAnnotation(Column.class);
            Attribute attribute = new Attribute(
                clAnnotation.columnName().isEmpty() ? field.getName().toLowerCase() : clAnnotation.columnName(),
                field.getName().toLowerCase(),
                clAnnotation.required(),
                field.getType()
            );

            if(field.isAnnotationPresent(Id.class)){
                idAttribute = attribute;
            }

            attributes.add(attribute);
        }
        return attributes;
    }

    public String joinAttributesNamesWithoutId(String limiter){
        return getAttributes()
                .stream()
                .filter(el -> !idAttribute.equals(el))
                .map(Attribute::getColumnName)
                .collect(Collectors.joining(limiter));
    }
    public Object getAttributeValue(T object, Attribute attribute) {
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

    protected T mapResultSetToInstance(ResultSet resultSet){
        Map<String, Object> values = new HashMap<>();
        getAttributes().forEach(attribute ->{
            try {
                Object value;
                if (attribute.getFieldType().isEnum()) {
                    String enumString = resultSet.getString(attribute.getColumnName());
                    value = Enum.valueOf((Class<Enum>) attribute.getFieldType(), enumString);
                } else {
                    value = resultSet.getObject(attribute.getColumnName());
                }
                values.put(attribute.getFieldName(), value);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to map resultSet");
            }
        });
        return createInstance(values);
    }
}