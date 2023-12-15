package mock.tests;

import model.*;
import repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AccountTest {
    private static final AccountRepository accountRepository = new AccountRepository();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        accountRepository.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update account test");
        System.out.println(accountRepository.saveAll(List.of(
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
        System.out.println(accountRepository.saveAll(List.of(
            new Account(
                null,
                "name_insertd",
                null,
                AccountType.BANK,
                new Currency("currency_ariary", null,null),
                null
            )
        ), null));
    }
    public static void doTransaction () throws SQLException {
        System.out.println(accountRepository.doTransaction("account_id1", new Transaction(
            null, "label_transaction", BigDecimal.valueOf(1000), null, TransactionType.CREDIT,
            new Category("category_id1", null, null)
        )));
    }

    public static void getBalance() throws SQLException {
        System.out.println(accountRepository.getBalance("account_id1", LocalDateTime.now()));
        System.out.println(accountRepository.getBalance(
            "account_id1",
            LocalDateTime.of(2022,01,01, 01, 01,01)
        ));
    }

    public static void getBalanceInterval() throws SQLException {
        System.out.println(accountRepository.getBalanceInterval(
            "account_id1",
            LocalDateTime.of(2022,01,01, 01, 01,01),
            LocalDateTime.now()
        ));

        System.out.println(accountRepository.getBalanceInterval(
            "account_id1",
            LocalDateTime.of(2022,01,01, 01, 01,01),
            LocalDateTime.of(2023,01,01, 01, 01,01)
        ));
    }

    public static void getCurrentBalance() throws SQLException {
        System.out.println(accountRepository.getCurrentBalance("account_id1"));
    }

    public static void getCategorySum() throws SQLException {
        accountRepository.getAllCategorySum("account_id",LocalDate.of(2023,01,01), LocalDate.now()).forEach(System.out::println);
    }

    public static void launch() throws SQLException {
        AccountTest.findAll();
        AccountTest.create();
        AccountTest.update();
        AccountTest.doTransaction();
        AccountTest.getBalance();
        AccountTest.getBalanceInterval();
        AccountTest.getCurrentBalance();
        AccountTest.getCategorySum();
    }
}
