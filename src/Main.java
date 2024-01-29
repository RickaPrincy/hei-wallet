import repository.CategoryCrudOperations;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations();
        System.out.println(categoryCrudOperations.findOne("category_2"));
    }
}