package repository;

import lombok.AllArgsConstructor;
import model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class AccountRepository implements BasicRepository<Account>{
    private final Connection connection = PostgresqlConnection.getConnection();
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
            !balanceList.isEmpty() ? balanceList.get(0) : new Balance("-", BigDecimal.valueOf(0), LocalDateTime.now()),
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

    public Account doTransaction(Transaction transaction, String accountId) throws SQLException {
        if(accountId != null){
            Map<String, Pair> accountFilter = Map.of(Query.ID_LABEL, new Pair(accountId, true));
            List<Account> accounts = findAll(accountFilter);
            if(accounts.isEmpty())
                return null;

            Account target = accounts.get(0);
            if(
                target.getType() != AccountType.BANK &&
                transaction.getType() == TransactionType.DEBIT &&
                target.getBalance().getAmount().compareTo(transaction.getAmount()) < 0
            ){
                return target;
            }

            //remove generated value
            transaction.setTransactionDatetime(null);
            transaction.setId(null);

            //persist new transaction
            transactionRepository.save(transaction, target.getId());

            //persist new balance
            BigDecimal amountToAdd = transaction.getType() == TransactionType.CREDIT ? transaction.getAmount() : transaction.getAmount().negate();
            BigDecimal newBalance = target.getBalance().getAmount().add(amountToAdd);
            balanceRepository.save(new Balance(
                null,
                newBalance,
                null
            ), target.getId());
            return findAll(accountFilter).get(0);
        }
        return null;
    }

    public List<Balance> getBalance(String accountId, LocalDateTime from, LocalDateTime to) throws SQLException {
        Map<String, Pair> accountFilter = Map.of(Query.ID_LABEL, new Pair(accountId, true));
        List<Account> accounts = findAll(accountFilter);
        if(accounts.isEmpty())
            return new ArrayList<>();
        String query = "SELECT * FROM \"" + BalanceRepository.TABLE_NAME + "\" WHERE \"" + BalanceRepository.ACCOUNT_LABEL + "\" = ? AND \"" + BalanceRepository.CREATION_DATETIME + "\" BETWEEN ? AND ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, accountId);
        statement.setTimestamp(2, Timestamp.valueOf(from));
        statement.setTimestamp(3, Timestamp.valueOf(to));
        ResultSet resultSet = statement.executeQuery();
        List<Balance> balances = new ArrayList<>();
        while(resultSet.next()){
            balances.add(BalanceRepository.createInstance(resultSet));
        }
        return balances;
    }

    public Balance getBalanceInDate(String accountId, LocalDateTime date) throws SQLException {
        Map<String, Pair> accountFilter = Map.of(Query.ID_LABEL, new Pair(accountId, true));
        List<Account> accounts = findAll(accountFilter);
        if(accounts.isEmpty())
            return null;
        String query = "SELECT * FROM \"" + BalanceRepository.TABLE_NAME + "\" WHERE \"" + Query.ID_LABEL + "\"= ? AND \"" + BalanceRepository.CREATION_DATETIME + "\" <= ? ORDER BY \"" + BalanceRepository.CREATION_DATETIME; PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, accountId);
        statement.setTimestamp(2, Timestamp.valueOf(date));
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        return BalanceRepository.createInstance(resultSet);
    }
}
