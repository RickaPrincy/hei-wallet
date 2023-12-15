package mock.tests;

import model.Transfer;
import repository.AccountCrudOperations;
import repository.TransferCrudOperations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransferTest {
    private static final TransferCrudOperations transferCrudOperations = new TransferCrudOperations();
    private static final AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        transferCrudOperations.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update transfer test");
        System.out.println(transferCrudOperations.save(
            new Transfer(
                "transfer_id1",
                BigDecimal.valueOf(251),
                null,
                accountCrudOperations.findById("account_id1"),
                accountCrudOperations.findById("account_id2")
            ), null)
        );
    }

    public static void create() throws SQLException {
        System.out.println("Insert transfer test");

        System.out.println(transferCrudOperations.save(
            new Transfer(
                null,
                BigDecimal.valueOf(251),
                LocalDateTime.now(),
                accountCrudOperations.findById("account_id1"),
                accountCrudOperations.findById("account_id2")
            ), null)
        );
    }

    public static void launch() throws SQLException {
        TransferTest.findAll();
        TransferTest.create();
        TransferTest.update();
    }
}