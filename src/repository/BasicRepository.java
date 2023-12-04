package repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BasicRepository <T>{
    List<T> findAll(Map<String, Pair> filters) throws SQLException;
    List<T> saveAll(List<T> toSave);
    T save(T toSave) throws SQLException;
}
