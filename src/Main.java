import mock.tests.*;
import model.Category;
import model.CategoryType;
import model.Transaction;
import model.TransactionType;
import repository.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
/*
        CurrencyTest.launch();
        BalanceTest.launch();
*/
/*
        TransactionTest.launch();
*/
/*
        AccountTest.launch();
*/
/*
        CategoryRepository repository = new CategoryRepository();
        System.out.println(repository.findAll());
*/
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        transactionRepository.save(
            new Transaction(
                null,
                "mine",
                BigDecimal.valueOf(500),
                null,
                TransactionType.CREDIT,
                new Category("category_1",null, CategoryType.ALL)
            ), "account_id1"
        );
/*
        accountRepository.findAll("WHERE \"id\"='account_id1'").get(0).getTransactions().forEach(System.out::println);
*/
    }
}