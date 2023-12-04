package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Query {
    private final static Connection connection = PostgresqlConnection.getConnection();
    public static String toSqlName(String text){
        return "\"" + text +  "\"";
    }
    public static String createKeyValue(Map<String, String> values) {
        return values.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
    }
    public static ResultSet selectAll(String tableName) throws SQLException {
        String query = "SELECT * FROM " + toSqlName(tableName) + ";";
        return connection.createStatement().executeQuery(query);
    }

    public static String saveOrUpdate(String tableName, Map<String, String> values) throws SQLException {
        String query;
        if(values.containsKey("id"))
            query = "UDPATE " + toSqlName(tableName) + " SET " + createKeyValue(values) + " WHERE \"id\"="+ values.get("id");
        else{
            String valuesQuery = values.keySet().stream().map(Query::toSqlName).collect(Collectors.joining(","));
            query = "INSERT INTO (" + toSqlName(tableName) + valuesQuery +
                    ") VALUES ( " + values.values().stream().map(Query::toSqlName).collect(Collectors.joining(","));
        }

        PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();

        ResultSet idStatement = statement.getGeneratedKeys();
        if(idStatement.next())
            return idStatement.getString(1);
        return null;
    }

    public static List<String> saveOrUpdateAll(List<String> tableNames, List<Map<String, String>> values) throws SQLException {
        if(tableNames.size() != values.size()){
            throw new RuntimeException("Number of tableNames must be same as number of values");
        }
        List<String> ids = new ArrayList<>();
        for(int i = 0; i < tableNames.size(); i++){
            ids.add(saveOrUpdate(tableNames.get(i), values.get(i)));
        }
        return ids;
    }
}