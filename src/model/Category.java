package model;

import fjpa.annotation.Column;
import fjpa.annotation.Entity;
import fjpa.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Category {
    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private CategoryType type;
}