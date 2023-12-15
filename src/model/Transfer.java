package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {
    private String id;
    private BigDecimal amount;
    private LocalDateTime creationDatetime;
    private Account source, destination;
}
