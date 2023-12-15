package mock.tests;

import model.*;
import repository.AccountCrudOperations;
import repository.CurrencyCrudOperations;

import java.sql.SQLException;
import java.util.List;

public class AccountTest {
    private static final AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    private static final CurrencyCrudOperations currencyCrudOperations= new CurrencyCrudOperations();
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


    public static void launch() throws SQLException {
        AccountTest.findAll();
        AccountTest.create();
        AccountTest.update();
    }
}
