import mock.tests.*;
import model.CurrencyValue;
import repository.CategoryRepository;
import repository.CurrencyValueRepository;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CategoryRepository categoryRepository = new CategoryRepository();
        categoryRepository.findAll().forEach(System.out::println);

        CurrencyValueRepository currencyValueRepository = new CurrencyValueRepository();
        currencyValueRepository.findAll().forEach(System.out::println);

        //CurrencyTest.launch();
        //BalanceTest.launch();
        //TransactionTest.launch();
/*
        AccountTest.launch();
*/
    }
}