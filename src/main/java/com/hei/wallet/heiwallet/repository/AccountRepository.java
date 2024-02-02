package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Currency;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AccountRepository extends FJPARepository<Account> {
    private final CurrencyRepository currencyRepository;

    public AccountRepository(
            StatementWrapper statementWrapper,
            CurrencyRepository currencyRepository
    ) {
        super(Account.class, statementWrapper);
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Object getAttributeValue(Account account, Attribute attribute) {
        return switch (attribute.getFieldName()) {
            case "currency" -> account.getCurrency().getId();
            default -> super.getAttributeValue(account, attribute);
        };
    }

    @Override
    protected Account mapResultSetToInstance(ResultSet resultSet) {
        try {
            Account account = super.mapResultSetToInstance(resultSet);
            Currency currency = currencyRepository.findByField(
                    "id",
                    resultSet.getString("currency"),
                    false
            ).stream().findFirst().orElseThrow(InternalServerErrorException::new);
            account.setCurrency(currency);
            return account;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
