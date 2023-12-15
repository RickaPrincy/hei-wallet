package repository;

import model.Balance;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class BalanceRepository implements CrudOperations<Balance> {
    public final static String
        AMOUNT_LABEL = "balance",
        DATETIME_LABEL="creation_datetime",
        ACCOUNT_LABEL= "account",
        TABLE_NAME = "balance_history";
    public static Balance createInstance(ResultSet resultSet){
        try {
            return new Balance(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static LinkedHashMap<String, Object> getMapValues(Balance balance, String meta){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            AMOUNT_LABEL, balance.getAmount(),
            ACCOUNT_LABEL, meta,
            DATETIME_LABEL, Timestamp.valueOf(LocalDateTime.now())
        ));

        if(balance.getId() != null){
            valuesKeys.put(Query.ID_LABEL, balance.getId());
        }
        return valuesKeys;
    }
    public static QueryValues updateQuery(Balance balance, String meta){
        LinkedHashMap<String, Object> balanceValues= BalanceRepository.getMapValues(balance, meta);
        String balanceQuery = Query.saveOrUpdate(BalanceRepository.TABLE_NAME,new ArrayList<>(balanceValues.keySet()));
        return new QueryValues( balanceQuery, new ArrayList<>(balanceValues.values()));
    }

    @Override
    public List<Balance> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(
            TABLE_NAME, filters.keySet().stream().toList(),
            suffix == null ? " ORDER BY " + Query.toSqlName(DATETIME_LABEL) + " DESC" : suffix
        );
        List<Balance> result = StatementWrapper.select(query, filters.values().stream().toList(), BalanceRepository::createInstance);

        if(result.isEmpty()){
            result.add(new Balance(null, BigDecimal.valueOf(0), LocalDateTime.now()));
        }
        return result;
    }

    public List<Balance> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<Balance> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }

    @Override
    public List<Balance> saveAll(List<Balance> toSave, String meta) throws SQLException {
        List<Balance> result = new ArrayList<>();
        for(Balance balance: toSave){
            Balance saved = save(balance, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Balance save(Balance toSave, String meta) throws SQLException {
        QueryValues queryValues = updateQuery(toSave, meta);
        ResultSet resultSet = StatementWrapper.update(queryValues.getQuery(), queryValues.getValues());
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}
