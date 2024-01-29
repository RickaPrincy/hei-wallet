package fjpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FJPARepository <T> extends ReflectModel<T>{
    public FJPARepository(Class<T> type) {
        super(type);
    }

    public List<T> findAll() throws SQLException {
        String query = "SELECT * FROM " + getTableName() + ";";
        return StatementWrapper.select(query,null, this::mapResultSetToInstance);
    }

    public T findOne(Object id) throws SQLException {
        String query = "SELECT * FROM " + getTableName() + " WHERE " + idAttribute.getColumnName() + "=?;";
        List<T> lists = StatementWrapper.select(query, List.of(id), this::mapResultSetToInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }

    public T save(T toSave) throws SQLException {
        final boolean isCreate = getAttributeValue(toSave, idAttribute) == null;
        String query = "";
        if(isCreate){
            query = "INSERT INTO " + getTableName()
                    + " (" + joinAttributesNamesWithoutId(",")
                    + ") VALUES ( ? " + " , ? ".repeat(getAttributes().size() - 1) + " );";
        }else{
            query = "UPDATE " + getTableName()
                    + " SET " + joinAttributesNamesWithoutId(" = ? , ")
                    + " = ? WHERE " + idAttribute.getColumnName() + " = ?";
        }

        List<Object> values = getAttributes()
                .stream()
                .filter(el -> !el.equals(idAttribute))
                .map(el -> getAttributeValue(toSave, el))
                .collect(Collectors.toList());
        values.add(getAttributeValue(toSave, idAttribute));

        ResultSet resultSet = StatementWrapper.update(query, values);
        if(!resultSet.next())
            return null;
        return mapResultSetToInstance(resultSet);
    }

    public List<T> saveAll(List<T> toSaves) throws SQLException {
        List<T> result = new ArrayList<>();
        for(T toSave: toSaves){
            result.add(save(toSave));
        }
        return result;
    }
}
