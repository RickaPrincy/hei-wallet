package repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BasicRepository <T>{
    List<T> findAll(Map<String, Object> filters, String suffix) throws SQLException;
    List<T> saveAll(List<T> toSave, String meta) throws SQLException;
    T save(T toSave, String meta) throws SQLException;
}
