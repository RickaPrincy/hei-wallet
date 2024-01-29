package fjpa;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Attribute {
    private String columnName;
    private String fieldName;
    private boolean required;
    private Class<?> fieldType;
}