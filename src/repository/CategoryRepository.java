package repository;

import lombok.AllArgsConstructor;
import model.Category;
import model.CategorySum;
import model.CategoryType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
public class CategoryRepository implements CrudOperations<Category> {
    public final static String TABLE_NAME="category", NAME_LABEL = "name", TYPE_LABEL = "type";

    private static Category createInstance(ResultSet resultSet) {
        try {
            return new Category(
                resultSet.getString(Query.ID_LABEL),
                resultSet.getString(NAME_LABEL),
                CategoryType.valueOf(resultSet.getString(TYPE_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static Map<String, Object> getMapValues(Category toSave,String meta){
        Map<String, Object> valuesKeys = new LinkedHashMap<>(Map.of(
            NAME_LABEL, toSave.getName(),
            TYPE_LABEL, toSave.getType()
        ));
        if(toSave.getId() != null)
            valuesKeys.put(Query.ID_LABEL, toSave.getId());
        return valuesKeys;
    }

    @Override
    public List<Category> findAll(Map<String, Object> filters, String suffix) throws SQLException {
        String query = Query.selectAll(TABLE_NAME,filters.keySet().stream().toList(), suffix);
        return StatementWrapper.select(query,filters.values().stream().toList(), CategoryRepository::createInstance);
    }

    public List<Category> findAll(String suffix) throws SQLException {
        return findAll(Query.emptyMapValues(), suffix);
    }
    public List<Category> findAll() throws SQLException {
        return findAll(Query.emptyMapValues(), null);
    }

    @Override
    public List<Category> saveAll(List<Category> toSave, String meta) throws SQLException {
        List<Category> result = new ArrayList<>();
        for (Category category : toSave) {
            Category saved = save(category, null);
            result.add(saved);
        }
        return result;
    }

    @Override
    public Category save(Category toSave, String meta) throws SQLException {
        Map<String, Object> valuesKeys = getMapValues(toSave, meta);
        String query = Query.saveOrUpdate(TABLE_NAME, valuesKeys.keySet().stream().toList());
        ResultSet resultSet = StatementWrapper.update(query, valuesKeys.values().stream().toList());

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }

}
