package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CategorySum {
    String category;
    BigDecimal amount;
}
