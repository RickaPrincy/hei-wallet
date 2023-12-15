package repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CrudOperations<T>{
    List<T> findAll() throws SQLException;
    List<T> saveAll(List<T> toSave, String meta) throws SQLException;
    T save(T toSave, String meta) throws SQLException;
    T findById(String id) throws SQLException;
}
