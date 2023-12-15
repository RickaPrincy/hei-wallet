package repository;

import model.Balance;
import model.Currency;
import model.Transaction;
import model.TransactionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionRepository implements BasicRepository<Transaction>{
    public final static String
            LBL_LABEL = "label",
            AMOUNT_LABEL= "amount",
            DATETIME_LABEL= "transaction_datetime",
            ACCOUNT_LABEL= "account",
            TYPE_LABEL= "type",
            TABLE_NAME="transaction";
    private static Transaction createInstance(ResultSet resultSet){
        try {
            return new Transaction(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getString(LBL_LABEL),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
                TransactionType.valueOf(resultSet.getString(TYPE_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static QueryValues updateQuery(Transaction transaction, String meta){
        LinkedHashMap<String, Object> transactionValues = TransactionRepository.getMapValues(transaction, meta);
        String transactionQuery = Query.saveOrUpdate(TABLE_NAME, new ArrayList<>(transactionValues.keySet()));
        return new QueryValues( transactionQuery, new ArrayList<>(transactionValues.values()));
    }

    public static LinkedHashMap<String, Object> getMapValues(Transaction transaction, String meta){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            AMOUNT_LABEL, transaction.getAmount() == null ? 0 : transaction.getAmount(),
            ACCOUNT_LABEL, meta,
            LBL_LABEL, transaction.getLabel(),
            TYPE_LABEL, transaction.getType(),
            DATETIME_LABEL, Timestamp.valueOf(LocalDateTime.now())
        ));

        if(transaction.getId() != null){
            valuesKeys.put(Query.ID_LABEL, transaction.getId());
        }
        return valuesKeys;
    }

    @Override
    public List<Transaction> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(TABLE_NAME, filters.keySet().stream().toList(), suffix);
        return StatementWrapper.select( query, filters.values().stream().toList(), TransactionRepository::createInstance);
    }

    public List<Transaction> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<Transaction> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }
    @Override
    public List<Transaction> saveAll(List<Transaction> toSave, String meta) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        for(Transaction transaction: toSave){
            Transaction saved = save(transaction, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Transaction save(Transaction toSave, String meta) throws SQLException {
        QueryValues queryValues = updateQuery(toSave, meta);
        ResultSet resultSet = StatementWrapper.update(queryValues.getQuery(), queryValues.getValues());
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}
