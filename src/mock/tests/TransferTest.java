package mock.tests;

import model.Account;
import model.Balance;
import model.Transfer;
import repository.AccountRepository;
import repository.TransferRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TransferTest {
    private static final TransferRepository transferRepository = new TransferRepository();
    private static final AccountRepository accountRepository = new AccountRepository();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        transferRepository.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update transfer test");
        System.out.println(transferRepository.save(
            new Transfer(
                "transfer_id1",
                BigDecimal.valueOf(251),
                null,
                accountRepository.findAll("WHERE \"id\"='account_id1'").get(0),
                accountRepository.findAll("WHERE \"id\"='account_id2'").get(0)
            )
        , "mon label"));
    }

    public static void create() throws SQLException {
        System.out.println("Insert transfer test");

        System.out.println(transferRepository.save(
            new Transfer(
                null,
                BigDecimal.valueOf(252),
                null,
                accountRepository.findAll("WHERE \"id\"='account_id1'").get(0),
                accountRepository.findAll("WHERE \"id\"='account_id2'").get(0)
            )
        , "mon label"));
    }

    public static void doTransfer() throws SQLException {
        Account source = accountRepository.findAll("WHERE \"id\"='account_id1'").get(0);
        Account dest = accountRepository.findAll("WHERE \"id\"='account_id2'").get(0);

        System.out.println(transferRepository.doTransfer(
            new Transfer(
                null,
                BigDecimal.valueOf(500),
                null,
                source,
                dest
            )
        , "mon label"));
    }

    public static void launch() throws SQLException {
        TransferTest.findAll();
        TransferTest.create();
        TransferTest.update();
    }
}
