package repository;

import model.*;
import model.Account;
import repository.exception.CategoryNotFoundException;
import repository.exception.CurrenyValueNotFoundException;
import repository.exception.NotEnoughBalanceException;
import repository.exception.SameAccountException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AccountCrudOperations implements CrudOperations<Account> {
    private static final CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations();
    private static final BalanceCrudOperations balanceCrudOperations= new BalanceCrudOperations();
    private static final TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations();
    private static final CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
    private static final TransferCrudOperations transferCrudOperations = new TransferCrudOperations();
    private final static String
        ID_LABEL="id",
        NAME_LABEL="name",
        TYPE_LABEL="type",
        CURRENCY_LABEL="currency";

    private static Account createInstance(ResultSet resultSet) {
        try {
            return new Account(
                resultSet.getString(ID_LABEL),
                resultSet.getString(NAME_LABEL),
                balanceCrudOperations.findOneByAccount(resultSet.getString(ID_LABEL)),
                AccountType.valueOf(resultSet.getString(TYPE_LABEL)),
                currencyCrudOperations.findById(resultSet.getString(CURRENCY_LABEL)),
                transactionCrudOperations.findByAccount(resultSet.getString(ID_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String createUpdateQuery(Account account){
        List<String> columns = new ArrayList<>();
        if(account.getName() != null)
            columns.add(NAME_LABEL);
        if(account.getType() != null)
            columns.add(TYPE_LABEL);
        if(account.getCurrency() != null && account.getCurrency().getId() != null)
            columns.add(CURRENCY_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<Account> findAll() throws SQLException {
        String query = "SELECT * FROM \"account\"";
        return StatementWrapper.select(query, null, AccountCrudOperations::createInstance); }

    @Override
    public Account findById(String id) throws SQLException {
        String query = "SELECT * FROM \"account\" WHERE \"id\"=?";
        List<Account> lists = StatementWrapper.select(query, List.of(id), AccountCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
    @Override
    public List<Account> saveAll(List<Account> toSave,String meta) throws SQLException {
        List<Account> result = new ArrayList<>();
        for (Account account : toSave) {
            Account saved = save(account, meta);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Account save(Account toSave, String meta) throws SQLException {
        String saveQuery= "INSERT INTO \"account\" (\"name\",\"type\", \"currency\") VALUES (?, ?, ?);";
        String updateQuery= "UPDATE \"account\" SET " + createUpdateQuery(toSave) + " WHERE \"id\"= ?";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getName());
        values.add(toSave.getType());
        values.add(toSave.getCurrency() != null ? toSave.getCurrency().getId() : null);
        values.add(toSave.getId());
        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
    public Account doTransaction(Transaction transaction, String accountId) throws SQLException, NotEnoughBalanceException {
        Account target = findById(accountId);
        if(target == null)
            return null;
        BigDecimal transactionValue = transaction.getType() == TransactionType.DEBIT ? transaction.getAmount().negate() : transaction.getAmount();

        if(target.getType() != AccountType.BANK
            && transaction.getType() == TransactionType.DEBIT
            && target.getBalance().getAmount().compareTo(transactionValue) < 0
        ){
            throw new NotEnoughBalanceException("Cannot have negative money if account type is not BANK");
        }

        Balance newBalance = new Balance(null, transactionValue.add(target.getBalance().getAmount()), null);
        balanceCrudOperations.save(newBalance, accountId);
        transactionCrudOperations.save(transaction, accountId);
        return findById(accountId);
    }

    public Balance getBalanceInterval(String accountId, LocalDateTime from, LocalDateTime to) throws SQLException {
        String query =
                "SELECT * FROM \"balance_history\" WHERE \"account\" = ? AND \"" +
                BalanceCrudOperations.DATETIME_LABEL +
                "\" BETWEEN ? AND ? ORDER BY \"" +  BalanceCrudOperations.DATETIME_LABEL +
                "\" DESC LIMIT 1";
        List<Object> values = List.of(accountId, from, to);
        List<Balance> balances = StatementWrapper.select(query, values, BalanceCrudOperations::createInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public Balance getBalanceInDate(String accountId, LocalDateTime dateTime) throws SQLException {
        String query =
            "SELECT * FROM \"balance_history\" WHERE \"account\"= ?" +
            " AND \"" + BalanceCrudOperations.DATETIME_LABEL +
            "\" <= ? ORDER BY \"" + BalanceCrudOperations.DATETIME_LABEL + "\" DESC LIMIT 1;";
        List<Object> values = List.of(accountId, dateTime);
        List<Balance> balances = StatementWrapper.select(query, values, BalanceCrudOperations::createInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public Balance getCurrentBalance(String accountId) throws SQLException {
        return getBalanceInDate(accountId, LocalDateTime.now());
    }

    public List<CategorySum> getAllCategorySum(String accountId, LocalDate from, LocalDate to) throws SQLException {
        String query = "SELECT * FROM sum_in_out_by_category(?, ? ,?);";
        return StatementWrapper.select(query, List.of(accountId, from, to), resultSet -> {
            try {
                String CATEGORY_LABEL = "category_name", TOTAL_AMOUNT="total_amount";
                return new CategorySum(
                    resultSet.getString(CATEGORY_LABEL),
                    resultSet.getBigDecimal(TOTAL_AMOUNT)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public List<CategorySum> getCategorySumWithJava(String accountId, LocalDate from, LocalDate to) throws SQLException {
        Account account = findById(accountId);
        if(account == null)
            return null;
        List<Transaction> transactions = account.getTransactions();
        HashMap<String, BigDecimal> categorySums = new HashMap<>();
        for(Category category: categoryCrudOperations.findAll()){
            categorySums.put(category.getName(), BigDecimal.valueOf(0));
        }
        transactions.forEach(el -> {
            if(
                el.getTransactionDatetime().toLocalDate().isAfter(to) ||
                el.getTransactionDatetime().toLocalDate().isBefore(from)
            ){
                return;
            }

            String categoryName = el.getCategory().getName();
            BigDecimal oldValue = categorySums.get(categoryName);
            categorySums.put(categoryName, oldValue.add(el.getAmount()));
        });
        return categorySums.entrySet()
            .stream().map(el -> new CategorySum(el.getKey(), el.getValue()))
            .collect(Collectors.toList());
    }

    public Transfer doTransfer(String sourceId, String targetId, BigDecimal amount, String categoryId, String label)
            throws SQLException, AccountNotFoundException, SameAccountException, CategoryNotFoundException, NotEnoughBalanceException, CurrenyValueNotFoundException {
        if(sourceId.equals(targetId))
            throw new SameAccountException("The source and destination account be the same");
        Account source = findById(sourceId);
        Account target = findById(targetId);
        if(source == null)
            throw new AccountNotFoundException("The Source account doesn't exist");
        if(target == null)
            throw new AccountNotFoundException("The destination account doesn't exist");

        Category category = categoryCrudOperations.findById(categoryId);
        if(category == null)
            throw new CategoryNotFoundException("The category doesn't exist");


        if(!target.getCurrency().getId().equals(source.getCurrency().getId())){
            amount = amount.multiply(currencyCrudOperations.getValueInDate(target.getCurrency().getId(), LocalDateTime.now()));
        }

        LocalDateTime dateTime = LocalDateTime.now();
        Transaction srcTransaction = new Transaction(
            null,
            label,
            amount,
            LocalDateTime.now(),
            TransactionType.DEBIT,
            category
        );

        Transaction destTransaction = new Transaction(
            null,
            label,
            amount,
            LocalDateTime.now(),
            TransactionType.CREDIT,
            category
        );

        doTransaction(srcTransaction, sourceId);
        doTransaction(destTransaction, targetId);
        Transfer transfer = new Transfer(null,amount, dateTime,source, target);
        return transferCrudOperations.save(transfer, categoryId);
    }
}