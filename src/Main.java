import mock.tests.*;
import model.Category;
import model.CategoryType;
import model.Transaction;
import model.TransactionType;
import repository.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        AccountTest.getCategorySum();
    }
}