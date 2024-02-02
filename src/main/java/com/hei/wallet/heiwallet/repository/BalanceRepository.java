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
import java.util.List;

@Repository
public class BalanceRepository extends FJPARepository<Balance> {
    public BalanceRepository(StatementWrapper statementWrapper) {
        super(Balance.class, statementWrapper);
    }

    public List<Balance> findAllByAccountId(String accountId) throws SQLException {
        return findByField("account", accountId);
    }

    @Override
    public List<Balance> findAll() throws SQLException {
        String query = selectAllQuery + " ORDER BY \"creation_datetime\" DESC";
        return statementWrapper.select(query, null, this::mapResultSetToInstance);
    }

    @Override
    public List<Balance> findByField(String fieldName, Object fieldValue, List<Class<?>> excludes) throws SQLException {
        String query = selectAllQuery + " WHERE " + fieldName  + " = ? ORDER BY \"creation_datetime\" DESC";
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
                    resultSet.getString("account"),
                    List.of(Balance.class)
            );
            balance.setAccount(account);
            return balance;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
