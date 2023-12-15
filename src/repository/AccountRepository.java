package repository;

import model.*;
import model.Currency;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class AccountRepository implements CrudOperations<Account> {
    private static final CurrencyRepository currencyRepository = new CurrencyRepository();
    private static final BalanceRepository balanceRepository= new BalanceRepository();
    private static final TransactionRepository transactionRepository = new TransactionRepository();
    private final static String
        TABLE_NAME ="account",
        NAME_LABEL="name",
        TYPE_LABEL="type",
        CURRENCY_LABEL="currency";

    private static Account createInstance(ResultSet resultSet) {
        try {
            String id = resultSet.getString(Query.ID_LABEL);
            Map<String, Object> balanceFilter = Map.of(BalanceRepository.ACCOUNT_LABEL, id);
            Map<String, Object> currencyFilter = Map.of(Query.ID_LABEL, resultSet.getString(CURRENCY_LABEL));
            Map<String, Object> transactionFilter = Map.of(TransactionRepository.ACCOUNT_LABEL, id);

            return new Account(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getString(NAME_LABEL),
                balanceRepository.findAll(balanceFilter,null).get(0),
                AccountType.valueOf(resultSet.getString(TYPE_LABEL)),
                currencyRepository.findAll(currencyFilter,null).get(0),
                transactionRepository.findAll(transactionFilter,null)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static LinkedHashMap<String, Object> getMapValues(Account account, String meta){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            NAME_LABEL, account.getName(),
            TYPE_LABEL, account.getType(),
            CURRENCY_LABEL, account.getCurrency().getId()
        ));
        if(account.getId() != null)
            valuesKeys.put(Query.ID_LABEL, account.getId());
        return valuesKeys;
    }

    @Override
    public List<Account> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(TABLE_NAME,filters.keySet().stream().toList(), suffix);
        return StatementWrapper.select(query, filters.values().stream().toList(), AccountRepository::createInstance);
    }

    public List<Account> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<Account> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }
    @Override
    public List<Account> saveAll(List<Account> toSave, String meta) throws SQLException {
        List<Account> result = new ArrayList<>();
        for(Account account: toSave){
            Account saved = save(account, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Account save(Account toSave, String meta) throws SQLException {
        Map<String, Object> valuesKeys = getMapValues(toSave, meta);
        String query = Query.saveOrUpdate(TABLE_NAME, valuesKeys.keySet().stream().toList());
        ResultSet resultSet = StatementWrapper.update(query, valuesKeys.values().stream().toList());

        if(resultSet.next()){
            String id = resultSet.getString(1);
            Currency currency = currencyRepository.findAll(Map.of(Query.ID_LABEL, toSave.getCurrency().getId()), null).get(0);
            Balance balance = balanceRepository.findAll(Map.of(BalanceRepository.ACCOUNT_LABEL, id), null).get(0);
            List<Transaction> transactions = transactionRepository.findAll( Map.of(TransactionRepository.ACCOUNT_LABEL, id), null);

            toSave.setId(id);
            toSave.setCurrency(currency);
            toSave.setBalance(balance);
            toSave.setTransactions(transactions);
        }
        return toSave;
    }

    public static QueryValues balanceAndTransactionQuery(Account target, Transaction transaction){
        BigDecimal transactionValue = transaction.getType() == TransactionType.DEBIT ? transaction.getAmount().negate() : transaction.getAmount();
        Balance newBalance = new Balance(null, transactionValue.add(target.getBalance().getAmount()), null);
        QueryValues balanceQuery = BalanceRepository.updateQuery(newBalance, target.getId());
        QueryValues transactionQuery = TransactionRepository.updateQuery(transaction, target.getId());

        //Concatenate queryValues
        String query = balanceQuery.getQuery() + transactionQuery.getQuery();
        List<Object> values = new ArrayList<>(balanceQuery.getValues());
        values.addAll(transactionQuery.getValues());

        return new QueryValues(query, values);
    }

    public Account doTransaction(String accountId, Transaction transaction) throws SQLException {
        List<Account> accounts = findAll(Map.of(Query.ID_LABEL, accountId), " LIMIT 1");
        if(accounts.isEmpty()){
            return null;
        }
        QueryValues queryValues = balanceAndTransactionQuery(accounts.get(0), transaction);
        StatementWrapper.update(Query.transaction(queryValues.getQuery()), queryValues.getValues());
        return findAll(Map.of(Query.ID_LABEL, accountId)," LIMIT 1").get(0);
    }

    public Balance getBalance(String accountId, LocalDateTime datetime) throws SQLException {
        String suffix =  " AND " + Query.toSqlName(BalanceRepository.DATETIME_LABEL) + " <= ? ORDER BY " + Query.toSqlName(BalanceRepository.DATETIME_LABEL) + " DESC LIMIT 1";
        PreparedStatement statement = StatementWrapper.prepared(
            Query.selectAll(
                BalanceRepository.TABLE_NAME,
                List.of(BalanceRepository.ACCOUNT_LABEL),
                suffix
            ), List.of(accountId, datetime)
        );
        List<Balance> balances = StatementWrapper.mapResultSet(statement.executeQuery(),BalanceRepository::createInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public Balance getBalanceInterval(String accountId, LocalDateTime from, LocalDateTime to) throws SQLException {
        String suffix =  " AND " + Query.toSqlName(BalanceRepository.DATETIME_LABEL) + " BETWEEN ? AND ? ORDER BY " + Query.toSqlName(BalanceRepository.DATETIME_LABEL) + " DESC LIMIT 1";
        PreparedStatement statement = StatementWrapper.prepared(
            Query.selectAll(
                BalanceRepository.TABLE_NAME,
                List.of(BalanceRepository.ACCOUNT_LABEL),
                suffix
            ), List.of(accountId, from, to)
        );
        List<Balance> balances = StatementWrapper.mapResultSet(statement.executeQuery(),BalanceRepository::createInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public Balance getCurrentBalance(String accountId) throws SQLException {
        return getBalance(accountId, LocalDateTime.now());
    }
}