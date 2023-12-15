package repository;

import model.Transfer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TransferCrudOperations implements CrudOperations<Transfer> {
    private static final AccountCrudOperations accountCrudOperations = new AccountCrudOperations();
    public final static String
            AMOUNT_LABEL= "amount",
            DATETIME_LABEL= "creation_datetime",
            DESTINATION_LABEL= "destination",
            SOURCE_LABEL= "source",
            ID_LABEL="id";

    private static Transfer createInstance(ResultSet resultSet){
        try {
            return new Transfer(
                resultSet.getString(ID_LABEL),
                resultSet.getBigDecimal(AMOUNT_LABEL),
                resultSet.getTimestamp(DATETIME_LABEL).toLocalDateTime(),
                accountCrudOperations.findById(resultSet.getString(SOURCE_LABEL)),
                accountCrudOperations.findById(resultSet.getString(DESTINATION_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String createUpdateQuery(Transfer transaction, String categoryId){
        List<String> columns = new ArrayList<>();
        if(transaction.getAmount() != null)
            columns.add(AMOUNT_LABEL);
        columns.add(DATETIME_LABEL);
        if(transaction.getDestination() != null && transaction.getDestination().getId() != null)
            columns.add(DESTINATION_LABEL);
        if(transaction.getSource() != null && transaction.getSource().getId() != null)
            columns.add(SOURCE_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    @Override
    public List<Transfer> findAll() throws SQLException {
        String query = "SELECT * FROM \"transfer\" ORDER BY \"creation_datetime\";";
        return StatementWrapper.select(query, null, TransferCrudOperations::createInstance);
    }

    @Override
    public Transfer findById(String id) throws SQLException {
        String query = "SELECT * FROM \"transfer\" WHERE \"id\"=?";
        List<Transfer> lists = StatementWrapper.select(query, List.of(id), TransferCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }

    @Override
    public List<Transfer> saveAll(List<Transfer> toSave, String categoryId) throws SQLException {
        List<Transfer> result = new ArrayList<>();
        for (Transfer transaction : toSave) {
            Transfer saved = save(transaction, categoryId);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Transfer save(Transfer toSave, String categoryId) throws SQLException {
        String saveQuery= "INSERT INTO \"transfer\"(\"amount\", \"creation_datetime\", \"destination\", \"source\") VALUES (?, ?, ?, ?);";
        String updateQuery= "UPDATE \"transfer\" SET " + createUpdateQuery(toSave, categoryId) + " WHERE \"id\"= ?;";
        List<Object> values = new ArrayList<>();
        values.add(toSave.getAmount());
        values.add(LocalDateTime.now());
        values.add(toSave.getDestination() != null ? toSave.getDestination().getId() : null);
        values.add(toSave.getSource() != null ? toSave.getSource().getId() : null);
        values.add(toSave.getId());

        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);
        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        toSave.setCreationDatetime(LocalDateTime.now());
        return toSave;
    }
}