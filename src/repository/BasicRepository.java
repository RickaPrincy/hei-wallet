package repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface BasicRepository <T>{
    List<T> findAll();
    List<T> saveAll(List<T> toSave);
    T save(T toSave);
    T delete(T toDelete);

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class BasicImplementation<T> implements BasicRepository<T> {
        private String tableName;
        private List<String> columns;

        @Override
        public List<T> findAll() {
            return null;
        }

        @Override
        public List<T> saveAll(List<T> toSave) {
            return null;
        }

        @Override
        public T save(T toSave) {
            return null;
        }

        @Override
        public T delete(T toDelete) {
            return null;
        }
    }
}
