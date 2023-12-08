package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatementWrapper {
    private static final Connection connection = PostgresqlConnection.getConnection();
    public static PreparedStatement prepared(String query, List<Object> values) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        if(values != null){
            for(int i = 1; i <= values.size(); i++){
                Object current = values.get(i -1);
                if(current.getClass().isEnum()){
                    statement.setObject(i, current, Types.OTHER);
                }else{
                    statement.setObject(i, current);
                }
            }
        }
        return statement;
    }

    public static <T> List<T> selectAll(String tableName, Map<String, Object> filters, String suffix, Function<ResultSet, T> mapper) throws SQLException {
        List<String> keys = filters != null ? new ArrayList<>(filters.keySet()) : null;
        List<Object> values = filters != null ? new ArrayList<>(filters.values()) : null;

        String query = Query.selectAll(tableName, keys, suffix);
        QueryResult queryResult = StatementWrapper.select(query, values);
        return StatementWrapper.mapResultSet(queryResult.getResult(), mapper);
    }
    public static QueryResult select(String query, List<Object> values) throws SQLException {
        PreparedStatement statement = prepared(query, values);
        ResultSet result = statement.executeQuery();
        ResultSet generated = statement.getGeneratedKeys();
        return new QueryResult(result, generated);
    }

    public static void saveOrUpdate(String tableName, Map<String, Object> values, Consumer<ResultSet> setter) throws SQLException {
        String query = Query.saveOrUpdate(tableName, new ArrayList<>(values.keySet()));
        ResultSet resultSet = StatementWrapper.update(query, new ArrayList<>(values.values()));
        setter.accept(resultSet);
    }

    public static ResultSet update(String query, List<Object> values) throws SQLException {
        PreparedStatement statement = StatementWrapper.prepared(query, values);
        statement.executeUpdate();
        return statement.getGeneratedKeys();
    }

    public static <T> List<T> mapResultSet(ResultSet resultSet, Function<ResultSet, T> mapper) throws SQLException {
        List<T> result = new ArrayList<>();
        while(resultSet.next()){
            result.add(mapper.apply(resultSet));
        }
        return result.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
