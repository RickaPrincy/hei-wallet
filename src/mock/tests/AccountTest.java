package mock.tests;

import model.*;
import repository.AccountCrudOperations;
import repository.CategoryCrudOperations;
import repository.CurrencyCrudOperations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AccountTest {
    private static final AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    private static final CurrencyCrudOperations currencyCrudOperations= new CurrencyCrudOperations();
    private static final CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        accountCrudOperations.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update account test");
        System.out.println(accountCrudOperations.saveAll(List.of(
            new Account(
                "account_id1",
                "name_updated",
                null,
                AccountType.BANK,
                new Currency("currency_ariary", null,null),
                null
            )
        ), null));
    }

    public static void create() throws SQLException {
        System.out.println("Insert account test");
        System.out.println(accountCrudOperations.saveAll(List.of(
            new Account(
                null,
                "name_inserted",
                null,
                AccountType.BANK,
                currencyCrudOperations.findById("currency_ariary"),
                null
            )
        ), null));
    }

    public static void doTransaction () throws SQLException {
        System.out.println(accountCrudOperations.findById("account_id1"));
        Transaction transaction = new Transaction(null, "label_transaction", BigDecimal.valueOf(3000), null, TransactionType.DEBIT, categoryCrudOperations.findById("category_1"));
        System.out.println(accountCrudOperations.doTransaction(transaction, "account_id1"));
    }

    public static void getBalanceInterval() throws SQLException {
        System.out.println(accountCrudOperations.getBalanceInterval(
            "account_id1",
            LocalDateTime.of(2022,1,1, 1, 1,1),
            LocalDateTime.now()
        ));

        System.out.println(accountCrudOperations.getBalanceInterval(
            "account_id1",
            LocalDateTime.of(2022,1,1, 1, 1,1),
            LocalDateTime.of(2023,1,1, 1, 1,1)
        ));
    }

    public static void getCurrentBalance() throws SQLException {
        System.out.println(accountCrudOperations.getCurrentBalance("account_id1"));
    }

    public static void getBalanceInDate() throws SQLException {
        System.out.println(accountCrudOperations.getBalanceInDate("account_id1", LocalDateTime.now()));
        System.out.println(accountCrudOperations.getBalanceInDate(
            "account_id1",
            LocalDateTime.of(2022,1,1, 1, 1,1)
        ));
    }

    public static void getCategorySum() throws SQLException {
        accountCrudOperations.getAllCategorySum("account_id1",LocalDate.of(2023,1,1), LocalDate.now()).forEach(System.out::println);
    }

    public static void getCategorySumWithJava() throws SQLException {
        accountCrudOperations.getCategorySumWithJava(
            "account_id1",
            LocalDate.of(2023,1,1),
            LocalDate.now()
        ).forEach(System.out::println);
    }

    public static void launch() throws SQLException {
        AccountTest.findAll();
        AccountTest.create();
        AccountTest.update();
        AccountTest.doTransaction();
        AccountTest.getBalanceInterval();
        AccountTest.getBalanceInDate();
        AccountTest.getCurrentBalance();
        AccountTest.getCategorySumWithJava();
    }
}
