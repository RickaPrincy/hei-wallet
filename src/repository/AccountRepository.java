package repository;

import lombok.AllArgsConstructor;
import model.Account;
import model.AccountType;
import model.Balance;
import model.Currency;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class AccountRepository implements BasicRepository<Account>{
    private final static String
        TABLE_NAME ="account",
        NAME_LABEL="name",
        TYPE_LABEL="type",
        CURRENCY_LABEL="currency";

    final private static BalanceRepository balanceRepository = new BalanceRepository();
    final private static TransactionRepository transactionRepository = new TransactionRepository();
    final private static CurrencyRepository currencyRepository = new CurrencyRepository();

    private static Account createInstance(ResultSet resultSet) throws SQLException {
        Map<String, Pair> balanceFilter = Map.of(BalanceRepository.ACCOUNT_LABEL, new Pair(resultSet.getString(Query.ID_LABEL), true));
        Map<String, Pair> transactionFilter = Map.of(TransactionRepository.ACCOUNT_LABEL, new Pair(resultSet.getString(Query.ID_LABEL), true));
        Map<String, Pair> currencyFilter = Map.of(Query.ID_LABEL, new Pair(resultSet.getString(CURRENCY_LABEL), true));

        List<Balance> balanceList = balanceRepository.findAll(balanceFilter);
        return new Account(
            resultSet.getString(Query.ID_LABEL),
            resultSet.getString(NAME_LABEL),
            AccountType.valueOf(resultSet.getString(TYPE_LABEL)),
            currencyRepository.findAll(currencyFilter).get(0),
            balanceList.size() > 0 ? balanceList.get(0) : new Balance("-", BigDecimal.valueOf(0), LocalDateTime.now()),
            transactionRepository.findAll(transactionFilter)
        );
    }

    @Override
    public List<Account> findAll(Map<String, Pair> filters) throws SQLException {
        List<Account> results = new ArrayList<>();
        ResultSet resultSet = Query.selectAll(TABLE_NAME, filters, null);
        while(resultSet.next()){
            results.add(createInstance(resultSet));
        }
        return results;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave, String meta) {
        List<Account> result = new ArrayList<>();
        toSave.forEach(el-> {
            try {
                result.add(save(el, meta));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
        return result;
    }

    @Override
    public Account save(Account toSave, String meta) throws SQLException {
        Map<String,Pair> values = Map.of(
            Query.ID_LABEL, new Pair(toSave.getId(), true),
            NAME_LABEL, new Pair(toSave.getName(), true),
            TYPE_LABEL, new Pair(toSave.getType().toString(),true),
            CURRENCY_LABEL, new Pair(toSave.getCurrency().getId(), true)
        );

        String id = Query.saveOrUpdate(TABLE_NAME, values);
        if(id != null)
            toSave.setId(id);
        return toSave;
    }
}
