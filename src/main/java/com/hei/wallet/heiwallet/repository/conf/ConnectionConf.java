package com.hei.wallet.heiwallet.repository.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ConnectionConf {
    private final ConnectionProperties connectionProperties;

    @Bean
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                this.connectionProperties.getUrl(),
                this.connectionProperties.getUsername(),
                this.connectionProperties.getPassword()
        );
    }

    public ConnectionConf(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}