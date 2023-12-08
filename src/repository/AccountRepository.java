package repository;

import lombok.AllArgsConstructor;
import model.Account;
import model.AccountType;
import model.Balance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class AccountRepository implements BasicRepository<Account>{
    private final static String
        NAME_LABEL = "name",
        TYPE_LABEL= "type",
        DATETIME_LABEL= "transaction_datetime",
        CURRENCY_LABEL= "currency";
    final private static CurrencyRepository currencyRepository = new CurrencyRepository();
    private static Account createInstance(ResultSet resultSet) throws SQLException {
/*
        Map<String, Pair> currencyFilter = Map.of(Query.ID_LABEL, new Pair(resultSet.getString(CURRENCY_LABEL), true));
        return new Account(
            resultSet.getString(Query.ID_LABEL),
            resultSet.getString(NAME_LABEL),
            resultSet.getString(),
            resultSet.getString("owner"),
            resultSet.getString("bank_name"),
            resultSet.getString("number"),
            resultSet.getDate("creation_date"),
            resultSet.getBigDecimal("balance"),
            AccountType.valueOf(resultSet.getString("type")),
            currencyRepository.findAll(currencyFilter).get(0)
        );
*/
        return null;
    }

    @Override
    public List<Account> findAll(Map<String, Pair> filters) throws SQLException {
/*
        List<Account> results = new ArrayList<>();
        ResultSet resultSet = Query.selectAll("account", filters);
        while(resultSet.next()){
            results.add(createInstance(resultSet));
        }
        return results;
*/
        return null;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
/*
        List<Account> result = new ArrayList<>();
        toSave.forEach(el-> {
            try {
                result.add(save(el));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
        return result;
*/
        return null;
    }

    @Override
    public Account save(Account toSave) throws SQLException {
/*
        Map<String,Pair> values = Map.of(
            "id", new Pair(toSave.getId(), true),
            "name", new Pair(toSave.getName(), true),
            "comment", new Pair(toSave.getComment(),true),
            "owner", new Pair(toSave.getOwner(), true),
            "bank_name", new Pair(toSave.getBankName(), true),
            "number", new Pair(toSave.getNumber(), true),
            "creation_date", new Pair(toSave.getCreation_date().toString(), true),
            "balance", new Pair(toSave.getBalance().toString(), false),
            "type", new Pair(toSave.getType().toString(), true),
            "currency", new Pair(toSave.getCurrency().getId(), true)
        );

        String id = Query.saveOrUpdate("account", values);

        if(id != null)
            toSave.setId(id);
        return toSave;
*/
        return null;
    }
}
