package repository;

import model.Balance;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class BalanceRepository implements BasicRepository<Balance>{
    public final static String
        AMOUNT_LABEL = "balance",
        DATETIME_LABEL="creation_datetime",
        ACCOUNT_LABEL= "account",
        TABLE_NAME = "balance_history";
    public static final Balance defaultBalance = new Balance("default", BigDecimal.valueOf(0), LocalDateTime.now());
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

    public static LinkedHashMap<String, Object> setMapValues(Balance balance, String meta){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
                AMOUNT_LABEL, balance.getAmount(),
                ACCOUNT_LABEL, meta
        ));

        if(balance.getId() != null){
            valuesKeys.put(DATETIME_LABEL, Timestamp.valueOf(LocalDateTime.now()));
            valuesKeys.put(Query.ID_LABEL, balance.getId());
        }
        return valuesKeys;
    }

    @Override
    public List<Balance> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        List<Balance> result = StatementWrapper.selectAll(
            TABLE_NAME,
            filters,
            suffix == null ? " ORDER BY " + Query.toSqlName(DATETIME_LABEL) + " DESC" : suffix,
            BalanceRepository::createInstance
        );

        if(result.isEmpty()){
            result.add(defaultBalance);
        }
        return result;
    }

    @Override
    public List<Balance> saveAll(List<Balance> toSave, String meta) {
        List<Balance> result = new ArrayList<>();
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
    public Balance save(Balance toSave, String meta) throws SQLException {
        StatementWrapper.saveOrUpdate(TABLE_NAME, setMapValues(toSave, meta), resultSet -> {
            try {
                if(resultSet.next()){
                    toSave.setId(resultSet.getString(1));
                    toSave.setUpdatedAt(resultSet.getTimestamp(3).toLocalDateTime());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return toSave;
    }
}
