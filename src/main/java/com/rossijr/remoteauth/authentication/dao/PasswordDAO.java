package com.rossijr.remoteauth.authentication.dao;

import com.rossijr.remoteauth.db.config.DbConfig;

import java.sql.Connection;
import java.util.UUID;

/**
 * DAO class for password
 * <p>
 * This class is responsible for performing the password queries
 */
public class PasswordDAO {
    /**
     * Method to change the password of the user
     * @param uuid UUID of the user
     * @param password new password of the user
     * @param connection open connection to the database
     * @return true if the password was successfully changed, false otherwise
     */
    public static boolean changePassword(UUID uuid, String password, Connection connection) {
        // Query to be performed
        var sql = "UPDATE " + DbConfig.getUserSchema() + "." + DbConfig.getUserTable() + " SET password = ? WHERE uuid = ?";
        try {
            // Generate the prepared statement and replace the placeholders
            // Used also to prevent SQL injection
            var pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.setObject(2, uuid);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during changePassword - class {" + PasswordDAO.class.getName() + "}");
            System.out.println("RemoteAuth --/ERROR/-- Query: " + sql);
            return false;
        }
    }
}
