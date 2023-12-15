package repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatementWrapper {
    private static final Connection connection = PostgresqlConnection.getConnection();
    public static PreparedStatement prepared(String query, List<Object> values) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        if(values != null){
            List<Object> filteredValues = values.stream().filter(Objects::nonNull).toList();
            for(int i = 1; i <= filteredValues.size(); i++){
                Object current = filteredValues.get(i -1);
                if(current instanceof LocalDateTime){
                    statement.setTimestamp(i, Timestamp.valueOf((LocalDateTime) current));
                }else if(current.getClass().isEnum()){
                    statement.setObject(i, current, Types.OTHER);
                }else{
                    statement.setObject(i, current);
                }
            }
        }
        return statement;
    }

    public static <T> List<T> select(String query, List<Object> values, Function<ResultSet, T> mapper) throws SQLException {
        return StatementWrapper.mapResultSet(prepared(query, values).executeQuery(), mapper);
    }

    public static ResultSet update(String query, List<Object> values) throws SQLException {
        PreparedStatement statement = prepared(query, values);
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
