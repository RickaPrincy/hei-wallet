package mock.tests;

import model.Transaction;
import model.TransactionType;
import repository.CategoryCrudOperations;
import repository.TransactionCrudOperations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TransactionTest {
    private static final TransactionCrudOperations transactionCrudOperationsS = new TransactionCrudOperations();
    private static final CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();

    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        transactionCrudOperationsS.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update transaction test");
        System.out.println(transactionCrudOperationsS.saveAll(List.of(
            new Transaction(
                "transaction_id1",
                "label",
                BigDecimal.valueOf(250),
                null,
                TransactionType.CREDIT,
                categoryCrudOperations.findById("category_1")
            )
        ), "account_id1"));
    }

    public static void create() throws SQLException {
        System.out.println("Insert transaction test");
        System.out.println(transactionCrudOperationsS.saveAll(List.of(
            new Transaction(
                null,
                "label_inserted",
                BigDecimal.valueOf(250),
                null,
                TransactionType.CREDIT,
                categoryCrudOperations.findById("category_1")
            )
        ), "account_id1"));
    }

    public static void launch() throws SQLException {
        TransactionTest.findAll();
        TransactionTest.create();
        TransactionTest.update();
    }
}