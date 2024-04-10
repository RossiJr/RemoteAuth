package com.rossijr.remoteauth.authentication.dao;

import com.rossijr.remoteauth.authentication.models.UserModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO class for login
 * <p>
 * This class is responsible for performing the login queries
 */
public class LoginDAO {
    /**
     * Method to perform the login query
     * @param username username of the user
     * @param password password of the user
     * @param conn open connection to the database
     * @return UserModel object if the user is authenticated, null otherwise
     */
    public static UserModel login(String username, String password, Connection conn) {
        // Query to be performed
        var  sql = "SELECT * FROM public.user WHERE username = ? AND password = ? limit 1";
        try {
            // Generate the prepared statement and replace the placeholders
            // Used also to prevent SQL injection
            var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            var resultSet = pstmt.executeQuery();
            // Verify if is there something in the result set
            if (!resultSet.next()) {
                return null;
            }
            return new UserModel(
                    resultSet.getObject("uuid", java.util.UUID.class),
                    resultSet.getString("username"),
                    resultSet.getString("password"));
        } catch (SQLException e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during performing query - class {" + LoginDAO.class.getName() + "}");
            System.out.println("RemoteAuth --/ERROR/-- Query: " + sql);
        }
        return null;
    }
}
