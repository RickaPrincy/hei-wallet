package repository;

import lombok.AllArgsConstructor;
import model.Account;
import model.Transaction;
import model.TransactionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TransactionRepository implements BasicRepository<Transaction>{
    private final static AccountRepository accountRepository = new AccountRepository();
    private static Transaction createInstance(ResultSet resultSet) throws SQLException {
        Map<String, Pair> sourceFilter = Map.of("id", new Pair(resultSet.getString("source_account"), true));
        Map<String, Pair> destinationFilter = Map.of("id", new Pair(resultSet.getString("destination_account"), true));
        List<Account> found = accountRepository.findAll(destinationFilter);

        return new Transaction(
            resultSet.getString("id"),
            resultSet.getString("description"),
            resultSet.getBigDecimal("amount"),
            accountRepository.findAll(sourceFilter).get(0),
            !found.isEmpty() ? found.get(0) : null,
            resultSet.getTimestamp("transaction_datetime"),
            TransactionType.valueOf(resultSet.getString("type"))
        );
    }

    @Override
    public List<Transaction> findAll(Map<String, Pair> filters) throws SQLException {
        List<Transaction> results = new ArrayList<>();
        ResultSet resultSet = Query.selectAll("transaction", filters);
        while(resultSet.next()){
            results.add(createInstance(resultSet));
        }
        return results;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> result = new ArrayList<>();
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
    public Transaction save(Transaction toSave) throws SQLException {
        Map<String,Pair> values = Map.of(
            "id", new Pair(toSave.getId(), true),
            "description", new Pair(toSave.getDescription(), true),
                "transaction_datetime", new Pair(toSave.getTransactionDatetime().toString(), true),
            "amount",new Pair(toSave.getAmount().toString(),false),
            "type",new Pair(toSave.getType().toString(),true),
            "source_account", new Pair(toSave.getSourceAccount().getId(), true),
            "destination_account", new Pair(
                toSave.getDestinationAccount() != null ? toSave.getDestinationAccount().getId(): null, false
            )
        );

        String id = Query.saveOrUpdate("transaction", values);

        if(id != null)
            toSave.setId(id);
        return toSave;
    }
}
