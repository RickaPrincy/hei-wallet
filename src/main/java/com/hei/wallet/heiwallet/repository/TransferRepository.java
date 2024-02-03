package com.hei.wallet.heiwallet.repository;

import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.fjpa.Attribute;
import com.hei.wallet.heiwallet.fjpa.FJPARepository;
import com.hei.wallet.heiwallet.fjpa.StatementWrapper;
import com.hei.wallet.heiwallet.model.Account;
import com.hei.wallet.heiwallet.model.Transfer;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
public class TransferRepository extends FJPARepository<Transfer> {
    private final static String CREATION_DATE_LABEL="creation_datetime";

    public List<Transfer> findTransferInInterval(Instant from, Instant to) throws SQLException {
        String query = selectAllQuery + " WHERE \"" + CREATION_DATE_LABEL +
                "\" BETWEEN ? AND ? ORDER BY \"" + CREATION_DATE_LABEL + "\" DESC;";
        List<Object> values = List.of(from, to);
        return statementWrapper.select(query, values, this::mapResultSetToInstance);
    }

    @Override
    public List<Transfer> findAll() throws SQLException {
        String query = selectAllQuery + " ORDER BY \"" + CREATION_DATE_LABEL + "\" DESC;";
        return statementWrapper.select(query, null, this::mapResultSetToInstance);
    }

    @Override
    protected Object getAttributeValue(Transfer transfer, Attribute attribute) {
        return switch(attribute.getFieldName()){
            case "source" -> transfer.getSource().getId();
            case "destination" -> transfer.getDestination().getId();
            default -> super.getAttributeValue(transfer, attribute);
        };
    }

    @Override
    protected Transfer mapResultSetToInstance(ResultSet resultSet, List<Class<?>> excludes) {
        Transfer transfer = super.mapResultSetToInstance(resultSet, excludes);

        if(excludes.contains(Account.class))
            return transfer;
        AccountRepository accountRepository = new AccountRepository(statementWrapper);
        try{
            transfer.setSource(
                    accountRepository.findById(resultSet.getString("source"))
            );
            transfer.setDestination(
                    accountRepository.findById(resultSet.getString("destination"))
            );
            return transfer;
        }catch (SQLException error){
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public TransferRepository(StatementWrapper statementWrapper) {
        super(Transfer.class, statementWrapper);
    }
}
