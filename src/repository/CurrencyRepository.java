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

    private static Currency createInstance(ResultSet resultSet){
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

    @Override
    public List<Currency> findAll(Map<String, Object> filters,String suffix) throws SQLException {
        return StatementWrapper.selectAll(TABLE_NAME, filters, suffix, CurrencyRepository::createInstance);
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave, String meta) {
        List<Currency> result = new ArrayList<>();
        toSave.forEach(el -> {
            try {
                result.add(save(el, null));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        return result;
    }

    @Override
    public Currency save(Currency toSave, String meta) throws SQLException {
        Map<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            NAME_LABEL, toSave.getName(),
            CODE_LABEL, toSave.getCode()
        ));

        if(toSave.getId() != null)
            valuesKeys.put(Query.ID_LABEL, toSave.getId());

        StatementWrapper.saveOrUpdate(TABLE_NAME, valuesKeys, resultSet -> {
            try {
                if(resultSet.next()){
                    toSave.setId(resultSet.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return toSave;
    }
}
