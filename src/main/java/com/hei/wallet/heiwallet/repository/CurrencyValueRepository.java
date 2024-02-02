package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.exception.NotFoundException;
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
    public CurrencyValueRepository(StatementWrapper statementWrapper) {
        super(CurrencyValue.class, statementWrapper);
    }

    @Override
    protected CurrencyValue mapResultSetToInstance(ResultSet resultSet) {
        try {
            CurrencyRepository currencyRepository = new CurrencyRepository(statementWrapper);
            CurrencyValue currencyValue = super.mapResultSetToInstance(resultSet);
            Currency source = currencyRepository.findByField(
                    "id",
                    resultSet.getString("source"),
                    false
            ).stream().findFirst().orElseThrow(NotFoundException::new);

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
