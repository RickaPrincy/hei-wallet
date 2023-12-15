package repository;

import lombok.AllArgsConstructor;
import model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class CurrencyRepository implements BasicRepository<Currency>{
    public final static String
        NAME_LABEL = "name",
        CODE_LABEL = "code",
        TABLE_NAME = "currency";

    private static Currency createInstance(ResultSet resultSet) {
        try {
            return new Currency(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getString(NAME_LABEL),
                resultSet.getString(CODE_LABEL)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static Map<String, Object> getMapValues(Currency toSave,String meta){
        Map<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            NAME_LABEL, toSave.getName(),
            CODE_LABEL, toSave.getCode()
        ));
        if(toSave.getId() != null)
            valuesKeys.put(Query.ID_LABEL, toSave.getId());
        return valuesKeys;
    }

    @Override
    public List<Currency> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(TABLE_NAME,filters.keySet().stream().toList(), suffix);
        return StatementWrapper.select(query,filters.values().stream().toList(), CurrencyRepository::createInstance);
    }

    public List<Currency> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<Currency> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave, String meta) throws SQLException {
        List<Currency> result = new ArrayList<>();
        for (Currency currency : toSave) {
            Currency saved = save(currency, null);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Currency save(Currency toSave, String meta) throws SQLException {
        Map<String, Object> valuesKeys = getMapValues(toSave, meta);
        String query = Query.saveOrUpdate(TABLE_NAME, valuesKeys.keySet().stream().toList());
        ResultSet resultSet = StatementWrapper.update(query, valuesKeys.values().stream().toList());

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}
