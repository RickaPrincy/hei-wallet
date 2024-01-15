package fjpa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FJPARepository <T> extends ReflectModel<T>{
    public FJPARepository(Class<T> type) {
        super(type);
    }

    public List<T> findAll() throws SQLException {
        String query = "SELECT * FROM " + getTableName() + ";";
        return StatementWrapper.select(query,null, resultSet -> {
            Map<String, Object> values = new HashMap<>();
            getAttributes().forEach(attribute ->{
                try {
                    Object value;
                    if (attribute.getFieldType().isEnum()) {
                        String enumString = resultSet.getString(attribute.getColumnName());
                        value = Enum.valueOf((Class<Enum>) attribute.getFieldType(), enumString);
                    } else {
                        value = resultSet.getObject(attribute.getColumnName());
                    }
                    values.put(attribute.getFieldName(), value);
                } catch (SQLException e) {
                    throw new RuntimeException("Faild to map resultset to type T");
                }
            });
            return createInstance(values);
        });
    }
}
