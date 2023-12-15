import mock.tests.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
/*
        //category test
        CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
        categoryCrudOperations.findAll().forEach(System.out::println);
        System.out.println(categoryCrudOperations.findById("category_1"));

        //currency value
        CurrencyCrudOperations currencyCrudOperations= new CurrencyCrudOperations();
        currencyCrudOperations.findAll().forEach(System.out::println);
        System.out.println(currencyCrudOperations.findById("currency_ariary"));

        //currency_value test
        CurrencyValueCrudOperations currencyValueCrudOperations= new CurrencyValueCrudOperations();
        currencyValueCrudOperations.findAll().forEach(System.out::println);
        System.out.println(currencyValueCrudOperations.findById("currency_value1"));
*/
        AccountTest.doTransfer();
    }
}