package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyValue {
    private String id;
    private LocalDateTime effectiveDatetime;
    private BigDecimal amount;
    private Currency source, destination;
}
