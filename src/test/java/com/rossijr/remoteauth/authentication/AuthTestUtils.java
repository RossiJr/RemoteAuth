package com.rossijr.remoteauth.authentication;

import com.rossijr.remoteauth.authentication.models.UserModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public class AuthTestUtils {
    private static final Logger logger = Logger.getLogger(AuthTestUtils.class.getName());

    public static Connection mockConnection() {
        Connection connection = null;
        try {
            logger.info("Loading the H2 driver and establishing connection");
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=PostgreSQL");
            logger.info("Connection established");
        } catch (ClassNotFoundException e) {
            logger.severe("Error during loading the H2 driver");
            e.printStackTrace();
        } catch (Exception e) {
            logger.severe("Error during establishing connection");
            e.printStackTrace();
        }
        return connection;
    }

    public static void databaseInitiation(Connection connection, UserModel userModel) {
        try {
            logger.info("Creating the schema and the user table");
            var SQL1 = "CREATE SCHEMA IF NOT EXISTS public";
            var SQL2_1 = "DROP TABLE IF EXISTS users CASCADE";
            var SQL2 = "CREATE TABLE IF NOT EXISTS users (uuid UUID PRIMARY KEY UNIQUE, username VARCHAR(255) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL)";
            var SQL3 = "INSERT INTO users (uuid, username, password) VALUES (?, ?, ?)";
            logger.info("Executing the queries");
            connection.prepareStatement(SQL1).executeUpdate();
            connection.prepareStatement(SQL2_1).executeUpdate();
            connection.prepareStatement(SQL2).executeUpdate();
            var pstmt = connection.prepareStatement(SQL3);
            pstmt.setString(1, userModel.getUuid().toString());
            pstmt.setString(2, userModel.getUsername());
            pstmt.setString(3, userModel.getPassword());
            logger.info("Query: " + pstmt);
            pstmt.executeUpdate();
            logger.info("Schema and user table created");
        } catch (Exception e) {
            logger.severe("Error during creating the schema and the user table");
            e.printStackTrace();
        }
    }

}
