package mock.tests;

import model.Category;
import model.Transaction;
import model.TransactionType;
import repository.CategoryRepository;
import repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TransactionTest {
    private static final TransactionRepository transactionRepository = new TransactionRepository();
    private static CategoryRepository categoryRepository = new CategoryRepository();
    public static Category category;

    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        transactionRepository.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update transaction test");
        System.out.println(transactionRepository.saveAll(List.of(
            new Transaction(
                "transaction_id1",
                "label",
                BigDecimal.valueOf(250),
                null,
                TransactionType.CREDIT,
                category
            )
        ), "account_id1"));
    }

    public static void create() throws SQLException {
        System.out.println("Insert transaction test");
        System.out.println(transactionRepository.saveAll(List.of(
            new Transaction(
                null,
                "label_inserted",
                BigDecimal.valueOf(250),
                null,
                TransactionType.CREDIT,
                category
            )
        ), "account_id1"));
    }

    public static void launch() throws SQLException {
        TransactionTest.findAll();
        TransactionTest.create();
        TransactionTest.update();
    }
}
