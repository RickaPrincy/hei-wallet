import model.Account;
import model.AccountType;
import model.Currency;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.Pair;
import repository.PostgresqlConnection;

import javax.accessibility.AccessibleComponent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        AccountRepository accountRepository = new AccountRepository();
        CurrencyRepository currencyRepository = new CurrencyRepository();
        Currency insertedCurrency = new Currency("currency_dollar", "name_inserted", "code_inserted", null, 100, Date.valueOf("2023-01-01"));

        //filter by id test
        currencyRepository
                .findAll(Map.of("id", new Pair("currency_ariary",true)))
                .forEach(System.out::println);

        //saveAll and save (save or update)
        currencyRepository.saveAll(List.of(
            insertedCurrency,
            new Currency("currency_ariary", "name_inserted", "code_inserted", null, 100, Date.valueOf("2023-01-01"))
        ));

        //findALlWith no filter
        currencyRepository.findAll(null).forEach(System.out::println);

        //insert account
        Account insertAccount = new Account(
            null,
            "nametest",
            "test",
            "test",
            "test",
            "test",
            Date.valueOf("2023-01-01"),
            BigDecimal.valueOf(5),
            AccountType.BUSINESS,
            insertedCurrency
        );
        accountRepository.save(insertAccount);

        //findAllAccount
        accountRepository.findAll(null).forEach(System.out::println);
    }
}