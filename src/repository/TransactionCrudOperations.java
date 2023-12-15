package repository;

import model.Transaction;
import model.TransactionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionCrudOperations implements CrudOperations<Transaction> {
    public static CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
    public final static String
            LBL_LABEL = "label",
            AMOUNT_LABEL= "amount",
            DATETIME_LABEL= "transaction_datetime",
            ACCOUNT_LABEL= "account",
            TYPE_LABEL= "type",
            CATEGORY_LABEL= "category",
            ID_LABEL = "id";
    private static Transaction createInstance(ResultSet resultSet){
        try {
            return new Transaction(
                resultSet.getString(ID_LABEL),
                resultSet.getString(LBL_LABEL),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
                TransactionType.valueOf(resultSet.getString(TYPE_LABEL)),
                categoryCrudOperations.findById(resultSet.getString(CATEGORY_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String createUpdateQuery(Transaction transaction, String accountId){
        List<String> columns = new ArrayList<>();
        if(transaction.getCategory() != null && transaction.getCategory().getId() != null)
            columns.add(CATEGORY_LABEL);
        columns.add(DATETIME_LABEL);
        if(transaction.getType() != null)
            columns.add(TYPE_LABEL);
        if(transaction.getAmount() != null)
            columns.add(AMOUNT_LABEL);
        if(transaction.getLabel() != null)
            columns.add(LBL_LABEL);
        if(accountId != null)
            columns.add(ACCOUNT_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        String query = "SELECT * FROM \"transaction\";";
        return StatementWrapper.select(query, null, TransactionCrudOperations::createInstance);
    }

    @Override
    public Transaction findById(String id) throws SQLException {
        String query = "SELECT * FROM \"transaction\" WHERE \"id\"=?";
        List<Transaction> lists = StatementWrapper.select(query, List.of(id), TransactionCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }

    public List<Transaction> findByAccount(String accountId) throws SQLException {
        String query = "SELECT * FROM \"transaction\" WHERE \"account\"=? ORDER BY \"transaction_datetime\" DESC";
        return StatementWrapper.select(query, List.of(accountId), TransactionCrudOperations::createInstance);
    }
    @Override
    public List<Transaction> saveAll(List<Transaction> toSave, String meta) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : toSave) {
            Transaction saved = save(transaction, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Transaction save(Transaction toSave, String accountId) throws SQLException {
        String saveQuery= "INSERT INTO \"transaction\"(\"category\", \"transaction_datetime\", \"type\", \"amount\", \"label\", \"account\") VALUES (?, ?, ?, ?, ?, ?);";
        String updateQuery= "UPDATE \"transaction\" SET " + createUpdateQuery(toSave, accountId) + " WHERE \"id\"= ?;";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getCategory() != null ? toSave.getCategory().getId() : null);
        values.add(LocalDateTime.now());
        values.add(toSave.getType());
        values.add(toSave.getAmount());
        values.add(toSave.getLabel());
        values.add(accountId);
        values.add(toSave.getId());

        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        toSave.setTransactionDatetime(LocalDateTime.now());
        return toSave;
    }
}
