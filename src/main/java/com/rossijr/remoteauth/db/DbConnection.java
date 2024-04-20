package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.db.config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Responsible to
 */
public class DbConnection{


    private final Connection connection;

    /**
     * Generates a connection to the database
     * @throws SQLException if it is not possible to connect to the database
     */
    public DbConnection() throws SQLException {
        try {
            var jdbcUrl = DbConfig.getDbUrl();
            var user = DbConfig.getDbUsername();
            var password = DbConfig.getDbPassword();
            this.connection = DriverManager.getConnection(jdbcUrl, user, password);
            if(connection == null) {
                throw new SQLException("Not possible to connect to the database");
            }
        } catch (SQLException e) {
            throw new SQLException("Error connecting to database", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
