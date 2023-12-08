import mock.tests.AccountTest;
import mock.tests.BalanceTest;
import mock.tests.CurrencyTest;
import mock.tests.TransactionTest;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CurrencyTest.findAll();
        BalanceTest.findAll();
        TransactionTest.findAll();
        AccountTest.findAll();
    }
}