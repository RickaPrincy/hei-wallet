import model.*;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.Pair;
import repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        AccountRepository accountRepository = new AccountRepository();
        CurrencyRepository currencyRepository = new CurrencyRepository();
        TransactionRepository transactionRepository= new TransactionRepository();
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
            "account_id1",
            "nametest",
            "test",
            "test",
            "test",
            "tttt5t",
            Date.valueOf("2023-01-01"),
            BigDecimal.valueOf(5),
            AccountType.BUSINESS,
            insertedCurrency
        );
        System.out.println(accountRepository.save(insertAccount));

        //findAllAccount
        accountRepository.findAll(null).forEach(System.out::println);

        //create transaction
        System.out.println(transactionRepository.saveAll(List.of(
                new Transaction(null, "test", BigDecimal.valueOf(555), insertAccount, null, Timestamp.valueOf("2023-01-01 01:01:01"), TransactionType.DEPOSIT)
        )));

        //findAllAccount
        transactionRepository.findAll(null).forEach(System.out::println);
    }
}