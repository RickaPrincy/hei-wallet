package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Date;

@Data
@AllArgsConstructor
public class currency {
    private String id;
    private String name;
    private String code;
    private String symbol;
    private float exchange_rate;
    private Date updated_at;

}
