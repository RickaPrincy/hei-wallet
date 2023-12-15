package repository;

import lombok.AllArgsConstructor;
import model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CurrencyCrudOperations implements CrudOperations<Currency> {
    public final static String ID_LABEL="id", NAME_LABEL = "name", CODE_LABEL = "code";

    private static Currency createInstance(ResultSet resultSet) {
        try {
            return new Currency(
                resultSet.getString(ID_LABEL),
                resultSet.getString(NAME_LABEL),
                resultSet.getString(CODE_LABEL)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String createUpdateQuery(Currency currency){
        List<String> columns = new ArrayList<>();
        if(currency.getName() != null)
            columns.add(NAME_LABEL);
        if(currency.getCode() != null)
            columns.add(CODE_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<Currency> findAll() throws SQLException {
        String query = "SELECT * FROM \"currency\"";
        return StatementWrapper.select(query, null, CurrencyCrudOperations::createInstance);
    }

    @Override
    public Currency findById(String id) throws SQLException {
        String query = "SELECT * FROM \"currency\" WHERE \"id\"=?";
        List<Currency> lists = StatementWrapper.select(query, List.of(id), CurrencyCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
    @Override
    public List<Currency> saveAll(List<Currency> toSave,String meta) throws SQLException {
        List<Currency> result = new ArrayList<>();
        for (Currency currency : toSave) {
            Currency saved = save(currency, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Currency save(Currency toSave, String meta) throws SQLException {
        String saveQuery= "INSERT INTO \"currency\" (\"name\",\"code\") VALUES (?, ?);";
        String updateQuery= "UPDATE \"currency\" SET " + createUpdateQuery(toSave) + " WHERE \"id\"= ?";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getName());
        values.add(toSave.getCode());
        values.add(toSave.getId());
        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}
