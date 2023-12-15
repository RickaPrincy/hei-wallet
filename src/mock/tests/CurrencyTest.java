package mock.tests;

import model.Currency;
import repository.CurrencyCrudOperations;
import java.sql.SQLException;
import java.util.List;

public class CurrencyTest {
    private static final CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        currencyCrudOperations.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update currency test");
        System.out.println(currencyCrudOperations.saveAll(List.of(
            new Currency("currency_dollar", "Dollar", "code_inserted")
        ), null));
    }

    public static void create() throws SQLException {
        System.out.println("Insert currency test");
        System.out.println(currencyCrudOperations.saveAll(List.of(
                new Currency(null, "name_inserted", "code_inserted")
        ), null));
    }

    public static void launch() throws SQLException {
        CurrencyTest.findAll();
        CurrencyTest.create();
        CurrencyTest.update();
    }
}
