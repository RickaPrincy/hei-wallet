import model.*;
import repository.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        CurrencyRepository currencyRepository = new CurrencyRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        BalanceRepository balanceRepository = new BalanceRepository();
        AccountRepository accountRepository= new AccountRepository();
/*
        Currency insertedCurrency = new Currency("currency_dollar", "name_inserted", "code_inserted");
*/

        //filter by id test
/*
        currencyRepository
                .findAll(Map.of(Query.ID_LABEL, new Pair("currency_ariary",true)))
                .forEach(System.out::println);
*/

        //saveAll and save (save or update)
/*
        currencyRepository.saveAll(List.of(
            insertedCurrency,
            new Currency("currency_ariary", "name_inserted", "code_inserted")
        ));
*/

        //findALlWith no filter
        currencyRepository.findAll(null).forEach(System.out::println);
        balanceRepository.findAll(null).forEach(System.out::println);
        transactionRepository.findAll(null).forEach(System.out::println);
        accountRepository.findAll(null).forEach(System.out::println);
/*
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.findAll(null).forEach(System.out::println);
*/
    }
}