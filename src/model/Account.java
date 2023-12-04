package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import repository.CurrencyRepository;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id, name, comment, owner, bankName, number;
    private Date creation_date;
    private BigDecimal balance;
    private AccountType type;
    private Currency currency;
}
