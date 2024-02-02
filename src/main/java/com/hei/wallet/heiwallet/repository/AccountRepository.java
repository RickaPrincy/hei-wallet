package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Balance;
import com.hei.wallet.heiwallet.model.Currency;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AccountRepository extends FJPARepository<Account> {

    public AccountRepository(StatementWrapper statementWrapper) {
        super(Account.class, statementWrapper);
    }

    @Override
    public Object getAttributeValue(Account account, Attribute attribute) {
        if(attribute.getFieldName().equals("currency"))
            return account.getCurrency().getId();
        return super.getAttributeValue(account, attribute);
    }

    @Override
    protected Account mapResultSetToInstance(ResultSet resultSet, List<Class<?>> excludes) {
        try {
            // Base of account
            Account account = super.mapResultSetToInstance(resultSet, excludes);

            // relation with currency
            CurrencyRepository currencyRepository = new CurrencyRepository(statementWrapper);
            Currency currency = currencyRepository.findById(resultSet.getString("currency"));
            account.setCurrency(currency);

            // relation with balance
            if(excludes.contains(Balance.class))
                return account;
            BalanceRepository balanceRepository = new BalanceRepository(statementWrapper);
            List<Balance> balances = balanceRepository.findByField(
                    "account",
                    account.getId(),
                    List.of(Account.class)
            );
            account.setBalances(balances);

            return account;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
