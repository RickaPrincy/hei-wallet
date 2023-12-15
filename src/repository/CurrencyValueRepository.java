package repository;

import model.CurrencyValue;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CurrencyValueRepository implements BasicRepository<CurrencyValue>{
    private static final CurrencyRepository currencyRepository= new CurrencyRepository();
    public final static String
            AMOUNT_LABEL = "amount",
            DATETIME_LABEL="effective_date",
            SRC_LABEL= "source",
            DEST_LABEL= "destination",
            TABLE_NAME = "currency_value";
    public static CurrencyValue createInstance(ResultSet resultSet){
        try {
            Map<String, Object> sourceFilter =  Map.of(Query.ID_LABEL, resultSet.getString(SRC_LABEL));
            Map<String, Object> destinationFilter =  Map.of(Query.ID_LABEL, resultSet.getString(DEST_LABEL));

            return new CurrencyValue(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                currencyRepository.findAll(sourceFilter, null).get(0),
                currencyRepository.findAll(destinationFilter, null).get(0)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static LinkedHashMap<String, Object> getMapValues(CurrencyValue currency, String meta){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            AMOUNT_LABEL, currency.getAmount(),
            DEST_LABEL, currency.getDestination().getId(),
            SRC_LABEL, currency.getSource().getId(),
            DATETIME_LABEL, Timestamp.valueOf(LocalDateTime.now())
        ));

        if(currency.getId() != null){
            valuesKeys.put(Query.ID_LABEL, currency.getId());
        }
        return valuesKeys;
    }
    public static QueryValues updateQuery(CurrencyValue currency, String meta){
        LinkedHashMap<String, Object> currencyValues= CurrencyValueRepository.getMapValues(currency, meta);
        String currencyQuery = Query.saveOrUpdate(CurrencyValueRepository.TABLE_NAME,new ArrayList<>(currencyValues.keySet()));
        return new QueryValues( currencyQuery, new ArrayList<>(currencyValues.values()));
    }

    @Override
    public List<CurrencyValue> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(
                TABLE_NAME, filters.keySet().stream().toList(),
                suffix == null ? " ORDER BY " + Query.toSqlName(DATETIME_LABEL) + " DESC" : suffix
        );
        return StatementWrapper.select(query, filters.values().stream().toList(), CurrencyValueRepository::createInstance);
    }

    public List<CurrencyValue> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<CurrencyValue> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }

    @Override
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave, String meta) throws SQLException {
        List<CurrencyValue> result = new ArrayList<>();
        for(CurrencyValue currency: toSave){
            CurrencyValue saved = save(currency, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave, String meta) throws SQLException {
        QueryValues queryValues = updateQuery(toSave, meta);
        ResultSet resultSet = StatementWrapper.update(queryValues.getQuery(), queryValues.getValues());
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}
