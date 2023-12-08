package repository;

import model.*;
import model.Currency;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class AccountRepository implements BasicRepository<Account>{
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
    public static LinkedHashMap<String, Object> setMapValues(Account account){
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
        return StatementWrapper.selectAll(
            TABLE_NAME,
            filters,
            suffix,
            AccountRepository::createInstance
        );
    }

    @Override
    public List<Account> saveAll(List<Account> toSave, String meta) {
        List<Account> result = new ArrayList<>();
        toSave.forEach(el -> {
            try {
                result.add(save(el, meta));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return result;
    }

    @Override
    public Account save(Account toSave, String meta) throws SQLException {
        StatementWrapper.saveOrUpdate(TABLE_NAME, setMapValues(toSave), resultSet -> {
            try {
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
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return toSave;
    }

    public void createBalanceAndTransaction(Account target, Transaction transaction) throws SQLException {
        if(
            transaction.getType() == TransactionType.DEBIT &&
            target.getType() != AccountType.BANK &&
            transaction.getAmount().compareTo(target.getBalance().getAmount()) < 0
        ){
            return;
        }
        BigDecimal transactionValue = transaction.getType() == TransactionType.DEBIT ? transaction.getAmount().negate() : transaction.getAmount();
        Balance newBalance = new Balance(null, transactionValue.add(target.getBalance().getAmount()), null);
        LinkedHashMap<String, Object> balanceValues= BalanceRepository.setMapValues( newBalance, target.getId());
        LinkedHashMap<String, Object> transactionValues= TransactionRepository.setMapValues(transaction, target.getId());

        //Create sql query
        String balanceQuery = Query.saveOrUpdate(BalanceRepository.TABLE_NAME,new ArrayList<>(balanceValues.keySet()));
        String transactionQuery = Query.saveOrUpdate(TransactionRepository.TABLE_NAME, new ArrayList<>(transactionValues.keySet()));

        //Concatenate and Map values to List<Object>
        List<Object> values = new ArrayList<>(new ArrayList<>(balanceValues.values()));
        values.addAll(new ArrayList<>(transactionValues.values()));

        //do update
        StatementWrapper.update(Query.transaction(List.of(balanceQuery, transactionQuery)), values);
    }

    public Account doTransaction(String accountId, Transaction transaction) throws SQLException {
        List<Account> accounts = findAll(Map.of(Query.ID_LABEL, accountId), " LIMIT 1");
        if(accounts.isEmpty()){
            return null;
        }
        createBalanceAndTransaction(accounts.get(0), transaction);
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