import fjpa.FJPARepository;
import model.Category;
import repository.CategoryCrudOperations;

import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
        categoryCrudOperations.findAll().forEach(System.out::println);
    }
}