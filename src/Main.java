import model.Category;
import model.CategoryType;
import repository.CategoryCrudOperations;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        final Category category = new Category(
                "category_1",
            "nam_inseted" ,
            CategoryType.ALL
        );

        CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
        categoryCrudOperations.findAll().forEach(System.out::println);
        System.out.println(categoryCrudOperations.saveAll(List.of(category)));
    }
}