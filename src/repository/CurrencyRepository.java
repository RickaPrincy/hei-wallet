package repository;

import lombok.AllArgsConstructor;
import model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CurrencyRepository implements BasicRepository<Currency>{
    private static Currency createInstance(ResultSet resultSet) throws SQLException {
        return new Currency(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("code"),
            resultSet.getString("symbol"),
            resultSet.getFloat("exchange_rate"),
            resultSet.getDate("updated_at")
        );
    }

    @Override
    public List<Currency> findAll(Map<String, Pair> filters) throws SQLException {
        List<Currency> results = new ArrayList<>();
        ResultSet resultSet = Query.selectAll("currency", filters);
        while(resultSet.next()){
            results.add(createInstance(resultSet));
        }
        return results;
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        List<Currency> result = new ArrayList<>();
        toSave.forEach(el-> {
            try {
                result.add(save(el));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
        return result;
    }

    @Override
    public Currency save(Currency toSave) throws SQLException {
        Map<String,Pair> values = Map.of(
            "id", new Pair(toSave.getId(), true),
            "name", new Pair(toSave.getName(), true),
            "code", new Pair(toSave.getCode(),true),
            "symbol", new Pair(toSave.getSymbol(), true),
            "exchange_rate", new Pair(String.valueOf(toSave.getExchangeRate()), false),
            "updated_at", new Pair(toSave.getUpdatedAt().toString(), true)
        );

        String id = Query.saveOrUpdate("currency", values);

        if(id != null)
            toSave.setId(id);
        return toSave;
    }
}
