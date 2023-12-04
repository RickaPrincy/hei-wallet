package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id, description;
    private BigDecimal amount;
    private Account sourceAccount, destinationAccount;
    private Timestamp transactionDatetime;
    private TransactionType type;
}
