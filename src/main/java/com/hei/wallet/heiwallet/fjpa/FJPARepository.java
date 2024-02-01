package com.hei.wallet.heiwallet.fjpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FJPARepository <T> extends ReflectModel<T> implements BasicCrudOperation<T> {
    protected final String selectAllQuery;
    protected final StatementWrapper statementWrapper;

    public FJPARepository(Class<T> type, StatementWrapper statementWrapper) {
        super(type);
        this.statementWrapper = statementWrapper;
        selectAllQuery = "SELECT * FROM " + getTableName();
    }

    @Override
    public List<T> findAll() throws SQLException {
        return statementWrapper.select(selectAllQuery + " ;",null, this::mapResultSetToInstance);
    }

    public List<T> findByField(String fieldName, Object fieldValue) throws SQLException {
        String query = selectAllQuery + " WHERE " + fieldName  + " = ? ;";
        return statementWrapper.select(query,List.of(fieldValue), this::mapResultSetToInstance);
    }

    @Override
    public T findById(Object id) throws SQLException {
        List<T> lists = findByField(getIdAttribute().getFieldName(), id);
        return lists.isEmpty() ? null : lists.get(0);
    }

    @Override
    public T saveOrUpdate(T toSave) throws SQLException {
        final Object idValue = getAttributeValue(toSave, getIdAttribute());
        if(idValue == null)
            return null;

        final boolean isCreate = findById(idValue) == null;

        String query;
        if(isCreate){
            query = "INSERT INTO " + getTableName()
                    + " (" + joinAttributesNames(",") + ") VALUES ( ? "
                    + " , ? ".repeat(getAttributes().size() - 1) + " );";
        }else{
            query = "UPDATE " + getTableName()
                    + " SET " + joinAttributesNamesWithoutId(" = ? , ")
                    + " = ? WHERE " + this.getIdAttribute().getColumnName() + " = ?";
        }

        List<Object> values;
        if(isCreate){
            values = getAttributes()
                    .stream()
                    .map(el -> getAttributeValue(toSave, el))
                    .collect(Collectors.toList());
        }else{
            values = getAttributes()
                .stream()
                    .filter(el -> !el.equals(getIdAttribute()))
                    .map(el -> getAttributeValue(toSave, el))
                    .collect(Collectors.toList());
            values.add(getAttributeValue(toSave, getIdAttribute()));
        }

        ResultSet resultSet = statementWrapper.update(query, values);
        if(!resultSet.next())
            return null;
        return mapResultSetToInstance(resultSet);
    }

    @Override
    public List<T> saveOrUpdateAll(List<T> toSaves) throws SQLException {
        List<T> result = new ArrayList<>();
        for(T toSave: toSaves){
            result.add(saveOrUpdate(toSave));
        }
        return result;
    }
}