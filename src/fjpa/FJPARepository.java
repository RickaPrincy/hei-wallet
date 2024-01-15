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
            return createInstance(values);
        });
    }
}
