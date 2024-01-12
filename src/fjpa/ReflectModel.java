package fjpa;

import fjpa.annotation.Column;
import fjpa.annotation.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to get metadata about the generic class
 * @param <T>
 */
@NoArgsConstructor
public class ReflectModel<T>{
    protected Class<T> type;
    protected String tableName;
    protected List<Attribute> attributes;

    public ReflectModel(Class<T> type) {
        this.type = type;
        this.tableName = getTableName();
        this.attributes = getReflectedAttributes();
    }

    public String getTableName(){
        if(!type.isAnnotationPresent(Entity.class))
            throw new RuntimeException("Class must be annoted with @Entity to be used with fjpa");
        Entity entity = type.getAnnotation(Entity.class);
        return entity.tableName().isEmpty() ? type.getSimpleName().toLowerCase() : entity.tableName();
    }

    private List<Attribute> getReflectedAttributes(){
        List<Attribute> attributes = new ArrayList<>();
        Field[] fields = Arrays.stream(type.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class))
            .toArray(Field[]::new);

        for(Field field: fields){
            Column clAnnotation = field.getAnnotation(Column.class);
            Attribute attribute = new Attribute(
                clAnnotation.columnName().isEmpty() ? field.getName().toLowerCase() : clAnnotation.columnName(),
                field.getName().toLowerCase(),
                clAnnotation.required()
            );
            attributes.add(attribute);
        }
        return attributes;
    }

    public List<Attribute> getRequiredAttributes(){
        return attributes.stream().filter(Attribute::isRequired).collect(Collectors.toList());
    }
}