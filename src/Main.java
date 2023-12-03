import repository.PostgresqlConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        Connection connection = PostgresqlConnection.getConnection();
    }
}