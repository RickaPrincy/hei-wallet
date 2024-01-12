package model;

import fjpa.annotation.Column;
import fjpa.annotation.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Category {
    @Column(required = false, columnName = "ricka")
    private String id;

    @Column
    private String name;

    @Column
    private CategoryType type;
}
