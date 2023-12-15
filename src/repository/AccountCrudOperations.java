package repository;

import model.*;
import model.Account;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AccountCrudOperations implements CrudOperations<Account> {
    private static final CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
    private static final BalanceCrudOperations balanceCrudOperations= new BalanceCrudOperations();
    private static final TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
    private final static String
        ID_LABEL="id",
        NAME_LABEL="name",
        TYPE_LABEL="type",
        CURRENCY_LABEL="currency";

    private static Account createInstance(ResultSet resultSet) {
        try {
            return new Account(
                resultSet.getString(ID_LABEL),
                resultSet.getString(NAME_LABEL),
                balanceCrudOperations.findOneByAccount(resultSet.getString(ID_LABEL)),
                AccountType.valueOf(resultSet.getString(TYPE_LABEL)),
                currencyCrudOperations.findById(resultSet.getString(CURRENCY_LABEL)),
                transactionCrudOperations.findByAccount(resultSet.getString(ID_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String createUpdateQuery(Account account){
        List<String> columns = new ArrayList<>();
        if(account.getName() != null)
            columns.add(NAME_LABEL);
        if(account.getType() != null)
            columns.add(TYPE_LABEL);
        if(account.getCurrency() != null && account.getCurrency().getId() != null)
            columns.add(CURRENCY_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<Account> findAll() throws SQLException {
        String query = "SELECT * FROM \"account\"";
        return StatementWrapper.select(query, null, AccountCrudOperations::createInstance);
    }

    @Override
    public Account findById(String id) throws SQLException {
        String query = "SELECT * FROM \"account\" WHERE \"id\"=?";
        List<Account> lists = StatementWrapper.select(query, List.of(id), AccountCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
    @Override
    public List<Account> saveAll(List<Account> toSave,String meta) throws SQLException {
        List<Account> result = new ArrayList<>();
        for (Account account : toSave) {
            Account saved = save(account, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Account save(Account toSave, String meta) throws SQLException {
        String saveQuery= "INSERT INTO \"account\" (\"name\",\"type\", \"currency\") VALUES (?, ?, ?);";
        String updateQuery= "UPDATE \"account\" SET " + createUpdateQuery(toSave) + " WHERE \"id\"= ?";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getName());
        values.add(toSave.getType());
        values.add(toSave.getCurrency() != null ? toSave.getCurrency().getId() : null);
        values.add(toSave.getId());
        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
    public Account doTransaction(Transaction transaction, String accountId) throws SQLException {
        Account target = findById(accountId);
        if(target == null)
            return null;
        BigDecimal transactionValue = transaction.getType() == TransactionType.DEBIT ? transaction.getAmount().negate() : transaction.getAmount();

        //TODO throw specifiq error
        if(target.getType() != AccountType.BANK
            && transaction.getType() == TransactionType.DEBIT
            && target.getBalance().getAmount().compareTo(transactionValue) < 0
        ){
            return null;
        }

        Balance newBalance = new Balance(null, transactionValue.add(target.getBalance().getAmount()), null);
        balanceCrudOperations.save(newBalance, accountId);
        transactionCrudOperations.save(transaction, accountId);
        return findById(accountId);
    }

    public Balance getBalanceInterval(String accountId, LocalDateTime from, LocalDateTime to) throws SQLException {
        String query =
                "SELECT * FROM \"balance_history\" WHERE \"account\" = ? AND \"" +
                BalanceCrudOperations.DATETIME_LABEL +
                "\" BETWEEN ? AND ? ORDER BY \"" +  BalanceCrudOperations.DATETIME_LABEL +
                "\" DESC LIMIT 1";
        List<Object> values = List.of(accountId, from, to);
        List<Balance> balances = StatementWrapper.select(query, values, BalanceCrudOperations::createInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public Balance getBalanceInDate(String accountId, LocalDateTime dateTime) throws SQLException {
        String query =
            "SELECT * FROM \"balance_history\" WHERE \"account\"= ?" +
            " AND \"" + BalanceCrudOperations.DATETIME_LABEL +
            "\" <= ? ORDER BY \"" + BalanceCrudOperations.DATETIME_LABEL + "\" DESC LIMIT 1;";
        List<Object> values = List.of(accountId, dateTime);
        List<Balance> balances = StatementWrapper.select(query, values, BalanceCrudOperations::createInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public Balance getCurrentBalance(String accountId) throws SQLException {
        return getBalanceInDate(accountId, LocalDateTime.now());
    }
}