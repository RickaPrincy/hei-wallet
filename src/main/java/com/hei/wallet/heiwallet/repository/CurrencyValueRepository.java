package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Currency;
import com.hei.wallet.heiwallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CurrencyValueRepository extends FJPARepository<CurrencyValue> {
    private final CurrencyRepository currencyRepository;

    public CurrencyValueRepository(
            StatementWrapper statementWrapper,
            CurrencyRepository currencyRepository
    ) {
        super(CurrencyValue.class, statementWrapper);
        this.currencyRepository = currencyRepository;
    }

    @Override
    protected CurrencyValue mapResultSetToInstance(ResultSet resultSet) {
        try {
            CurrencyValue currencyValue = super.mapResultSetToInstance(resultSet);
            Currency source = currencyRepository.findById(resultSet.getString("source"));
            Currency destination = currencyRepository.findById(resultSet.getString("destination"));
            currencyValue.setDestination(destination);
            currencyValue.setSource(source);
        return currencyValue;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getAttributeValue(CurrencyValue currencyValue, Attribute attribute) {
        return switch (attribute.getFieldName()) {
            case "source" -> currencyValue.getSource().getId();
            case "destination" -> currencyValue.getDestination().getId();
            default -> super.getAttributeValue(currencyValue, attribute);
        };
    }
}
