package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Currency;
import com.hei.wallet.heiwallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CurrencyValueRepository extends FJPARepository<CurrencyValue> {
    public CurrencyValueRepository(StatementWrapper statementWrapper) {
        super(CurrencyValue.class, statementWrapper);
    }

    public List<CurrencyValue> findAllByCurrencyId(String currencyId) throws SQLException {
        String query = selectAllQuery + " WHERE \"source\" = ? OR \"destination\" = ? ORDER BY \"effective_date\" DESC";
        return statementWrapper.select(query, List.of(currencyId, currencyId), this::mapResultSetToInstance);
    }

    // start of configuration
    @Override
    protected CurrencyValue mapResultSetToInstance(ResultSet resultSet, List<Class<?>> excludes) {
        CurrencyValue currencyValue = super.mapResultSetToInstance(resultSet, excludes);
        try {
            CurrencyRepository currencyRepository = new CurrencyRepository(statementWrapper);
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
