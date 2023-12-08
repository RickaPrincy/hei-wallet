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
    private final static String
        LBL_LABEL = "label",
        AMOUNT_LABEL= "amount",
        DATETIME_LABEL= "transaction_datetime",
        ACCOUNT_LABEL= "account",
        TYPE_LABEL= "type",
        TABLE_NAME="transaction";
    private final static AccountRepository accountRepository = new AccountRepository();
    private static Transaction createInstance(ResultSet resultSet) throws SQLException {
        Map<String, Pair> accountFilter = Map.of(Query.ID_LABEL, new Pair(resultSet.getString(ACCOUNT_LABEL), true));
        List<Account> found = accountRepository.findAll(accountFilter);

        return new Transaction(
            resultSet.getString(Query.ID_LABEL),
            resultSet.getString(LBL_LABEL),
            resultSet.getBigDecimal(AMOUNT_LABEL),
            resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
            TransactionType.valueOf(resultSet.getString(TYPE_LABEL)),
            found.get(0)
        );
    }

    @Override
    public List<Transaction> findAll(Map<String, Pair> filters) throws SQLException {
        List<Transaction> results = new ArrayList<>();
        ResultSet resultSet = Query.selectAll(TABLE_NAME, filters);
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
            Query.ID_LABEL, new Pair(toSave.getId(), true),
            LBL_LABEL, new Pair(toSave.getLabel(), true),
            DATETIME_LABEL, new Pair(toSave.getTransactionDatetime().toString(), true),
            TYPE_LABEL,new Pair(toSave.getType().toString(),true),
            ACCOUNT_LABEL, new Pair(toSave.getAccount().getId(), true),
            AMOUNT_LABEL, new Pair(toSave.getAmount().toString(), false)
        );

        String id = Query.saveOrUpdate(TABLE_NAME, values);

        if(id != null)
            toSave.setId(id);
        return toSave;
    }
}
