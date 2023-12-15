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
    private static AccountRepository accountRepository = new AccountRepository();
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

    public static LinkedHashMap<String, Object> getMapValues(Transfer transfer){
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

    public static QueryValues updateQuery(Transfer transfer, String meta){
        LinkedHashMap<String, Object> transferValues = getMapValues(transfer);
        String transferQuery = Query.saveOrUpdate(TABLE_NAME, new ArrayList<>(transferValues.keySet()));
        return new QueryValues( transferQuery, new ArrayList<>(transferValues.values()));
    }

    @Override
    public List<Transfer> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(TABLE_NAME,filters.keySet().stream().toList(), suffix);
        return StatementWrapper.select(query, filters.values().stream().toList(), TransferRepository::createInstance);
    }

    public List<Transfer> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<Transfer> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }

    @Override
    public List<Transfer> saveAll(List<Transfer> toSave, String meta) throws SQLException {
        List<Transfer> result = new ArrayList<>();
        for (Transfer transfer: toSave) {
            Transfer saved = save(transfer, null);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Transfer save(Transfer toSave, String label) throws SQLException {
        QueryValues queryValues = updateQuery(toSave, label);
        ResultSet resultSet = StatementWrapper.update(queryValues.getQuery(), queryValues.getValues());
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }

    public Transfer doTransfer(Transfer toSave, String label) throws SQLException {
/*
        if(toSave.getSource().getType().equals(toSave.getDestination().getType())){

        }
*/
        Transaction srcTransaction = new Transaction(null, label, toSave.getAmount(), null,TransactionType.DEBIT);
        Transaction destTransaction = new Transaction(null, label, toSave.getAmount(), null,TransactionType.CREDIT);
        QueryValues srcQuery = AccountRepository.balanceAndTransactionQuery(toSave.getSource(), srcTransaction);
        QueryValues dstQuery = AccountRepository.balanceAndTransactionQuery(toSave.getSource(), destTransaction);
        QueryValues transferQuery = updateQuery(toSave, null);
        System.out.println(srcQuery.getQuery());
        System.out.println(srcQuery.getValues());

        String query =  Query.transaction(List.of(srcQuery.getQuery(), dstQuery.getQuery(), transferQuery.getQuery()));
        List<Object> values = srcQuery.getValues();
        values.addAll(dstQuery.getValues());
        values.addAll(transferQuery.getValues());
        ResultSet resultSet = StatementWrapper.update(query, values);
        if(resultSet.next()){
            toSave.setId(resultSet.getString(1));
        }
        return toSave;
    }
}
