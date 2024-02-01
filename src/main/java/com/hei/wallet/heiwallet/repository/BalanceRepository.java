package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Balance;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
public class BalanceRepository extends FJPARepository<Balance> {
    public BalanceRepository(StatementWrapper statementWrapper) {
        super(Balance.class, statementWrapper);
    }

    @Override
    public List<Balance> findAll() throws SQLException {
        String query = selectAllQuery + " ORDER BY \"creation_datetime\" DESC";
        return statementWrapper.select(query, null, this::mapResultSetToInstance);
    }

    @Override
    public Balance saveOrUpdate(Balance toSave) throws SQLException {
        toSave.setCreationDatetime(Instant.now());
        return super.saveOrUpdate(toSave);
    };

    @Override
    public List<Balance> findByField(String fieldName, Object fieldValue) throws SQLException {
        String query = selectAllQuery + " WHERE " + fieldName  + " = ? ORDER BY \"creation_datetime\" DESC";
        return statementWrapper.select(query, List.of(fieldValue), this::mapResultSetToInstance);
    }
}
