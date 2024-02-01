package com.hei.wallet.heiwallet.repository.conf;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConf {
    final public static String DB_URL = System.getenv("DB_URL");
    final public static String DB_PASSWORD = System.getenv("DB_PASSWORD");
    final public static String DB_USERNAME = System.getenv("DB_USERNAME");

    @Bean
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DB_URL,
                DB_USERNAME,
                DB_PASSWORD
        );
    }

}
