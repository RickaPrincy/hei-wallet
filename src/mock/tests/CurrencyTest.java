package mock.tests;

import model.Currency;
import repository.CurrencyRepository;
import repository.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CurrencyTest {
    private static final CurrencyRepository currencyRepository = new CurrencyRepository();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        currencyRepository.findAll(null, null).forEach(System.out::println);


        System.out.println("\nFind all with id filters");
        Map<String, Object> idFilter = Map.of(Query.ID_LABEL, "currency_dollar");
        currencyRepository.findAll(idFilter, null).forEach(System.out::println);

        System.out.println("\nFind all with id and name filters");
        Map<String, Object> idNameFilters = Map.of(
            Query.ID_LABEL, "currency_dollar",
            CurrencyRepository.NAME_LABEL, "Dollar US"
        );
        currencyRepository.findAll(idNameFilters, null).forEach(System.out::println);
    }

    public static void update(){
        System.out.println("Update currency test");
        System.out.println(currencyRepository.saveAll(List.of(
            new Currency("currency_dollar", "Dollar", "code_inserted")
        ), null));
    }

    public static void create(){
        System.out.println("Insert currency test");
        System.out.println(currencyRepository.saveAll(List.of(
                new Currency(null, "name_inserted", "code_inserted")
        ), null));
    }

    public static void launch() throws SQLException {
        CurrencyTest.findAll();
        CurrencyTest.create();
        CurrencyTest.update();
    }
}
