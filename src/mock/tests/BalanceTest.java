package mock.tests;

import model.Balance;
import repository.BalanceRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class BalanceTest {
    private static final BalanceRepository balanceRepository = new BalanceRepository();
    public static void findAll() throws SQLException {
        System.out.println("\"Find all with no filters params: ");
        balanceRepository.findAll().forEach(System.out::println);
    }

    public static void update() throws SQLException {
        System.out.println("Update balance test");
        System.out.println(balanceRepository.saveAll(List.of(
            new Balance("history_entry_1", BigDecimal.valueOf(250),null)
        ), "account_id1"));
    }

    public static void create() throws SQLException {
        System.out.println("Insert balance test");
        System.out.println(balanceRepository.saveAll(List.of(
            new Balance(null, BigDecimal.valueOf(200), null)
        ), "account_id1"));
    }

    public static void launch() throws SQLException {
        BalanceTest.findAll();
        BalanceTest.create();
        BalanceTest.update();
    }
}
