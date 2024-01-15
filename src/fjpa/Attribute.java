package fjpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Attribute {
    private String columnName;
    private String fieldName;
    private boolean required;
    private Class<?> fieldType;
}