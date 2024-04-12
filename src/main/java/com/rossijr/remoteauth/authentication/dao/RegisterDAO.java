package com.rossijr.remoteauth.authentication.dao;

import com.rossijr.remoteauth.db.config.DbConfig;

import java.sql.Connection;
import java.util.UUID;

/**
 * DAO class for register
 * <p>
 * This class is responsible for performing the register queries
 */
public class RegisterDAO {
    /**
     * Method to perform the register query
     * @param uuid UUID of the user
     * @param username username of the user
     * @param password password of the user
     * @param conn open connection to the database
     * @return true if the user was successfully registered, false otherwise
     */
    public static boolean register(UUID uuid, String username, String password, Connection conn) {
        // Query to be performed
        var sql = "INSERT INTO " + DbConfig.getUserSchema() + "." + DbConfig.getUserTable() + " (uuid, username, password) VALUES (?, ?, ?)";
        try {
            // Generate the prepared statement and replace the placeholders
            // Used also to prevent SQL injection
            var pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, uuid);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            int insertedRow = pstmt.executeUpdate();
            // Verify if the row was inserted, if true, the user was successfully registered
            if (insertedRow > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during register - class {" + RegisterDAO.class.getName() + "}");
            System.out.println("RemoteAuth --/ERROR/-- Query: " + sql);
        }
        return false;
    }

    /**
     * Check if the username already exists in the database
     * @param username username to be checked
     * @param conn open connection to the database
     * @return true if the username already exists, false otherwise
     */
    public static boolean usernameExists(String username, Connection conn) throws Exception {
        var sql = "SELECT * FROM " + DbConfig.getUserSchema() + "." + DbConfig.getUserTable()
                + " WHERE " + DbConfig.getUsernameColumn() + " = ? limit 1";
        try {
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            var resultSet = pstmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during usernameExists - class {" + RegisterDAO.class.getName() + "}");
            throw new Exception(e);
        }
    }

    /**
     * Check if the UUID already exists in the database
     * @param uuid UUID to be checked
     * @param conn opened connection to the database
     * @return true if the UUID already exists, false otherwise
     * @throws Exception if an error occurs during the query
     */
    public static boolean uuidExists(UUID uuid, Connection conn) throws Exception {
        var sql = "SELECT * FROM " + DbConfig.getUserSchema() + "." + DbConfig.getUserTable()
                + " WHERE " + DbConfig.getUUIDColumn() + " = ? limit 1";
        try {
            var pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, uuid);
            var resultSet = pstmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during uuidExists - class {" + RegisterDAO.class.getName() + "}");
            throw new Exception(e);
        }
    }
}