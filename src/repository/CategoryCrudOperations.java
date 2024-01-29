package repository;

import fjpa.FJPARepository;
import model.Category;
import model.CategoryType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryCrudOperations extends FJPARepository<Category> {
    public final static String ID_LABEL="id", NAME_LABEL = "name", TYPE_LABEL = "type";

    public CategoryCrudOperations() {
        super(Category.class);
    }

    private static Category createInstance(ResultSet resultSet) {
        try {
            return new Category(
                resultSet.getString(ID_LABEL),
                resultSet.getString(NAME_LABEL),
                CategoryType.valueOf(resultSet.getString(TYPE_LABEL))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String createUpdateQuery(Category category){
        List<String> columns = new ArrayList<>();
        if(category.getName() != null)
            columns.add(NAME_LABEL);
        if(category.getType() != null)
            columns.add(TYPE_LABEL);
        return columns.stream().map(el -> "\"" + el + "\" = ?").collect(Collectors.joining(", "));
    }

    public Category findById(String id) throws SQLException {
        String query = "SELECT * FROM \"category\" WHERE \"id\"=?";
        List<Category> lists = StatementWrapper.select(query, List.of(id), CategoryCrudOperations::createInstance);
        if(lists.isEmpty())
            return null;
        return lists.get(0);
    }
    public List<Category> saveAll(List<Category> toSave, String meta) throws SQLException {
        List<Category> result = new ArrayList<>();
        for (Category category : toSave) {
            Category saved = save(category, meta);
            result.add(saved);
        }
        return result;
    }

    public Category save(Category toSave, String meta) throws SQLException {
        String saveQuery= "INSERT INTO \"category\" (\"name\",\"type\") VALUES (?, ?);";
        String updateQuery= "UPDATE \"category\" SET " + createUpdateQuery(toSave) + " WHERE \"id\"= ?";
        List<Object> values = List.of(toSave.getName(), toSave.getType(), toSave.getId());
        ResultSet resultSet = StatementWrapper.update(toSave.getId() == null ? saveQuery : updateQuery, values);

        if(resultSet.next())
            toSave.setId(resultSet.getString(1));
        return toSave;
    }
}