import mock.tests.*;
import repository.CategoryRepository;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
/*
        CurrencyTest.launch();
        BalanceTest.launch();
*/
        TransactionTest.launch();
/*
        AccountTest.launch();
*/
/*
        CategoryRepository repository = new CategoryRepository();
        System.out.println(repository.findAll());
*/
    }
}