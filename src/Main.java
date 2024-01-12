import fjpa.ReflectModel;
import model.Category;

public class Main {
    public static void main(String[] args){
        ReflectModel<Category> test = new ReflectModel<>(Category.class);
        test.getRequiredAttributes().forEach(System.out::println);
        System.out.println(test.getTableName());
    }
}