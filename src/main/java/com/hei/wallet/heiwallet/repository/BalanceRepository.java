package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Balance;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
 public class BalanceRepository extends FJPARepository<Balance> {
    private final static String CREATION_DATE_LABEL="creation_datetime", ACCOUNT_LABEL="account";
    public List<Balance> findBalanceInInterval(String accountId, Instant from, Instant to) throws SQLException {
        String query = selectAllQuery + " WHERE \"" + ACCOUNT_LABEL + "\" = ? AND \"" +
                        CREATION_DATE_LABEL + "\" BETWEEN ? AND ? ORDER BY \"" + CREATION_DATE_LABEL + "\" DESC;";
        List<Object> values = List.of(accountId, from, to);
        return statementWrapper.select(query, values, this::mapResultSetToInstance);
    }

    public Balance findBalanceInDate(String accountId, Instant dateTime) throws SQLException {
        String query = selectAllQuery + " WHERE \"" + ACCOUNT_LABEL + "\" = ? " +
                        " AND \"" + CREATION_DATE_LABEL + "\" <= ? ORDER BY \"" + CREATION_DATE_LABEL + "\" DESC LIMIT 1;";
        List<Object> values = List.of(accountId, dateTime);
        List<Balance> balances = statementWrapper.select(query, values, this::mapResultSetToInstance);
        return balances.isEmpty() ? null : balances.get(0);
    }

    public List<Balance> findAllByAccountId(String accountId) throws SQLException {
        return findByField("account", accountId);
    }

    @Override
    public List<Balance> findAll() throws SQLException {
        String query = selectAllQuery + " ORDER BY \"" + CREATION_DATE_LABEL + "\" DESC;";
        return statementWrapper.select(query, null, this::mapResultSetToInstance);
    }

    @Override
    public List<Balance> findByField(String fieldName, Object fieldValue, List<Class<?>> excludes) throws SQLException {
        String query = selectAllQuery + " WHERE " + fieldName  + " = ? ORDER BY \"" + CREATION_DATE_LABEL + "\" DESC;";
        return statementWrapper.select(
                query,
                List.of(fieldValue),
                resultSet -> this.mapResultSetToInstance(resultSet,excludes)
        );
    }

    @Override
    protected Object getAttributeValue(Balance balance, Attribute attribute) {
        return switch(attribute.getFieldName()){
            case "account" -> balance.getAccount().getId();
            default -> super.getAttributeValue(balance, attribute);
        };
    }

    @Override
    protected Balance mapResultSetToInstance(ResultSet resultSet, List<Class<?>> excludes) {
        Balance balance = super.mapResultSetToInstance(resultSet, excludes);
        try {
            if(excludes.contains(Account.class))
                return balance;
            AccountRepository accountRepository = new AccountRepository(statementWrapper);
            Account account = accountRepository.findById(
                    resultSet.getString(ACCOUNT_LABEL),
                    List.of(Balance.class)
            );
            balance.setAccount(account);
            return balance;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public BalanceRepository(StatementWrapper statementWrapper) {
        super(Balance.class, statementWrapper);
    }
}
