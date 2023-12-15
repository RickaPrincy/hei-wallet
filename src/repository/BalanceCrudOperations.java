package repository;

import com.sun.source.tree.LiteralTree;
import model.Balance;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BalanceCrudOperations implements CrudOperations<Balance> {
    public final static String
        AMOUNT_LABEL = "balance",
        DATETIME_LABEL="creation_datetime",
        ACCOUNT_LABEL= "account",
        ID_LABEL = "id";
    public static Balance createInstance(ResultSet resultSet){
        try {
            return new Balance(
                resultSet.getString(ID_LABEL),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String createUpdateQuery(Balance balance, String accountId){
        List<String> columns = new ArrayList<>();
        if(balance.getAmount() != null)
            columns.add(AMOUNT_LABEL);
        columns.add(DATETIME_LABEL);
        if(accountId != null)
            columns.add(ACCOUNT_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<Balance> findAll() throws SQLException {
        String query = "SELECT * FROM \"balance_history\" ORDER BY \"creation_datetime\"";
        return StatementWrapper.select(query, null, BalanceCrudOperations::createInstance);
    }

    @Override
    public Balance findById(String id) throws SQLException {
        String query = "SELECT * FROM \"balance_history\" WHERE \"id\"=?";
        List<Balance> lists = StatementWrapper.select(query, List.of(id), BalanceCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
    @Override
    public List<Balance> saveAll(List<Balance> toSave, String accountId) throws SQLException {
        List<Balance> result = new ArrayList<>();
        for (Balance balance : toSave) {
            Balance saved = save(balance, accountId);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Balance save(Balance toSave, String accountId) throws SQLException {
        String saveQuery= "INSERT INTO \"balance_history\" (\"balance\",\"creation_datetime\", \"account\") VALUES (?, ?, ?);";
        String updateQuery= "UPDATE \"balance_history\" SET " + createUpdateQuery(toSave, accountId) + " WHERE \"id\"= ?";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getAmount());
        values.add(LocalDateTime.now());
        values.add(accountId);
        values.add(toSave.getId());

        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        toSave.setUpdatedAt(LocalDateTime.now());
        return toSave;
    }

    public Balance findOneByAccount(String accountId) throws SQLException {
        String query = "SELECT * FROM \"balance_history\" WHERE \"account\"=? ORDER BY \"creation_datetime\" DESC LIMIT 1";
        List<Balance> lists = StatementWrapper.select(query, List.of(accountId), BalanceCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
}
