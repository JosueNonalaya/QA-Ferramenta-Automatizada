package br.com.autoaudit.database;

import br.com.autoaudit.config.DataBaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DataBaseConfig.getDatabaseUrl()
        );
    }
}