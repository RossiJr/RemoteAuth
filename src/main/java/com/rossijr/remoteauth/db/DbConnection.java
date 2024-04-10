package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.db.config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Responsible to
 */
public class DbConnection {
    /**
     * Connect to the database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection connect() throws SQLException {
        try {
            var jdbcUrl = DbConfig.getDbUrl();
            var user = DbConfig.getDbUsername();
            var password = DbConfig.getDbPassword();
            var connection = DriverManager.getConnection(jdbcUrl, user, password);
            if(connection != null) {
                return connection;
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new SQLException("Error connecting to database", e);
        }
    }
}
