package repository;

import model.CurrencyValue;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class CurrencyValueCrudOperations implements CrudOperations<CurrencyValue> {
    private static final CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
    public final static String
            AMOUNT_LABEL = "amount",
            DATETIME_LABEL="effective_date",
            SRC_LABEL= "source",
            DEST_LABEL= "destination";
    public static CurrencyValue createInstance(ResultSet resultSet){
        try {
            return new CurrencyValue(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                currencyCrudOperations.findById(resultSet.getString(SRC_LABEL)),
                currencyCrudOperations.findById(resultSet.getString(DEST_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String createUpdateQuery(CurrencyValue currencyValue){
        List<String> columns = new ArrayList<>();
        if(currencyValue.getDestination() != null && currencyValue.getDestination().getId() != null)
            columns.add(DEST_LABEL);
        if(currencyValue.getSource() != null && currencyValue.getSource().getId() != null)
            columns.add(SRC_LABEL);
        if(currencyValue.getDateTime() != null)
            columns.add(DATETIME_LABEL);
        if(currencyValue.getAmount() != null)
            columns.add(AMOUNT_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<CurrencyValue> findAll() throws SQLException {
        String query = "SELECT * FROM \"currency_value\";";
        return StatementWrapper.select(query, null, CurrencyValueCrudOperations::createInstance);
    }

    @Override
    public CurrencyValue findById(String id) throws SQLException {
        String query = "SELECT * FROM \"currency_value\" WHERE \"id\"=?";
        List<CurrencyValue> lists = StatementWrapper.select(query, List.of(id), CurrencyValueCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
    @Override
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave, String meta) throws SQLException {
        List<CurrencyValue> result = new ArrayList<>();
        for (CurrencyValue currencyValue : toSave) {
            CurrencyValue saved = save(currencyValue, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave, String meta) throws SQLException {
        String saveQuery= "INSERT INTO \"currency_value\"(\"destination\", \"source\", \"effective_date\", \"amount\") VALUES (?, ?, ?, ?);";
        String updateQuery= "UPDATE \"currency\" SET " + createUpdateQuery(toSave) + " WHERE \"id\"= ?";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getDestination() != null ? toSave.getDestination().getId() : null);
        values.add(toSave.getSource() != null ? toSave.getSource().getId() : null);
        values.add(toSave.getDateTime());
        values.add(toSave.getAmount());
        values.add(toSave.getId());

        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}
