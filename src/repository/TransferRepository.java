package repository;

import model.Transaction;
import model.TransactionType;
import model.Transfer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class TransferRepository implements BasicRepository<Transfer>{
    @Override
    public List<Transfer> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        return null;
    }

    @Override
    public List<Transfer> saveAll(List<Transfer> toSave, String meta) throws SQLException {
        return null;
    }

    @Override
    public Transfer save(Transfer toSave, String meta) throws SQLException {
        return null;
    }
    /*private static AccountRepository accountRepository = new AccountRepository();
    public final static String
        AMOUNT_LABEL= "amount",
        DATETIME_LABEL= "creation_datetime",
        DESTINATION_LABEL= "destination",
        SOURCE_LABEL= "source",
        TABLE_NAME="transfer";
    private static Transfer createInstance(ResultSet resultSet){
        try {
            return new Transfer(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
                accountRepository.findAll(Map.of(Query.ID_LABEL, resultSet.getString(SOURCE_LABEL)),null).get(0),
                accountRepository.findAll(Map.of(Query.ID_LABEL, resultSet.getString(DESTINATION_LABEL)),null).get(0)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static LinkedHashMap<String, Object> setMapValues(Transfer transfer){
        LinkedHashMap<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            AMOUNT_LABEL, transfer.getAmount() == null ? 0 : transfer.getAmount(),
            SOURCE_LABEL, transfer.getSource().getId(),
            DESTINATION_LABEL, transfer.getDestination().getId(),
            DATETIME_LABEL, Timestamp.valueOf(LocalDateTime.now())
        ));

        if(transfer.getId() != null){
            valuesKeys.put(Query.ID_LABEL, transfer.getId());
        }
        return valuesKeys;
    }

    @Override
    public List<Transfer> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        return StatementWrapper.selectAll( TABLE_NAME, filters, suffix, TransferRepository::createInstance);
    }

    @Override
    public List<Transfer> saveAll(List<Transfer> toSave, String meta) {
        List<Transfer> result = new ArrayList<>();
        toSave.forEach(el -> {
            try {
                result.add(save(el, meta));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        return result;
    }

    @Override
    public Transfer save(Transfer toSave, String label) throws SQLException {
        String query = """
            BEGIN;
                INSERT INTO "balance"("
        """;
        return null;
    */
}
