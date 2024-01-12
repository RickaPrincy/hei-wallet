package model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account{
    private String id;
    private String name;
    private Balance balance;
    private AccountType type;
    private Currency currency;
    private List<Transaction> transactions;
}
