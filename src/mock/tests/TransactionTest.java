package mock.tests;

import model.Transaction;
import model.TransactionType;
import repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TransactionTest {
    private static final TransactionRepository transactionRepository = new TransactionRepository();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        transactionRepository.findAll(null, null).forEach(System.out::println);
    }

    public static void update(){
        System.out.println("Update transaction test");
        System.out.println(transactionRepository.saveAll(List.of(
            new Transaction("transaction_id1", "label", BigDecimal.valueOf(250), null,TransactionType.CREDIT)
        ), "account_id1"));
    }

    public static void create(){
        System.out.println("Insert transaction test");
        System.out.println(transactionRepository.saveAll(List.of(
            new Transaction(null, "label_inserted", BigDecimal.valueOf(250), null, TransactionType.CREDIT)
        ), "account_id1"));
    }

    public static void launch() throws SQLException {
        TransactionTest.findAll();
        TransactionTest.create();
        TransactionTest.update();
    }
}
