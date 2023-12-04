import model.Currency;
import repository.CurrencyRepository;
import repository.PostgresqlConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        CurrencyRepository currencyRepository = new CurrencyRepository();
        currencyRepository.findAll(null).forEach(System.out::println);
        currencyRepository.saveAll(List.of(
            new Currency("currency_dollar", "name_inserted", "code_inserted", null, 100, Date.valueOf("2023-01-01")),
            new Currency("currency_ariary", "name_inserted", "code_inserted", null, 100, Date.valueOf("2023-01-01"))
        ));
        currencyRepository.findAll(null).forEach(System.out::println);
    }
}