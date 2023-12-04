package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id, ref, description;
    private Double amount;
    private Account sourceAccount, destinationAccount;
    private Timestamp transactionDatetime;
}
