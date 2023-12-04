package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private String id, name, symbol;
    private float exchangeRate;
    private Date updatedAt;

}
