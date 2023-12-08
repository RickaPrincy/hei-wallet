package repository;

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

    public static LinkedHashMap<String, Object> setMapValues(Transaction transaction, String meta){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            AMOUNT_LABEL, transaction.getAmount() == null ? 0 : transaction.getAmount(),
            ACCOUNT_LABEL, meta,
            LBL_LABEL, transaction.getLabel(),
            TYPE_LABEL, transaction.getType()
        ));

        if(transaction.getId() != null){
            valuesKeys.put(DATETIME_LABEL, Timestamp.valueOf(LocalDateTime.now()));
            valuesKeys.put(Query.ID_LABEL, transaction.getId());
        }
        return valuesKeys;
    }

    @Override
    public List<Transaction> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        return StatementWrapper.selectAll( TABLE_NAME, filters, suffix, TransactionRepository::createInstance);
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave, String meta) {
        List<Transaction> result = new ArrayList<>();
        toSave.forEach(el -> {
            try {
                result.add(save(el, meta));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        return result;
    }

    @Override
    public Transaction save(Transaction toSave, String meta) throws SQLException {
        StatementWrapper.saveOrUpdate(TABLE_NAME, setMapValues(toSave, meta), resultSet -> {
            try {
                if(resultSet.next()){
                    toSave.setId(resultSet.getString(1));
                    toSave.setTransactionDatetime(resultSet.getTimestamp(2).toLocalDateTime());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return toSave;
    }
}
