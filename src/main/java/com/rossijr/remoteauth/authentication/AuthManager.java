package com.rossijr.remoteauth.authentication;

import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.db.GenericDAO;
import com.rossijr.remoteauth.db.Utils;
import com.rossijr.remoteauth.db.annotations.model_annotation.Column;
import com.rossijr.remoteauth.db.annotations.model_annotation.Table;
import com.rossijr.remoteauth.db.config.DbConfig;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

/**
 * <p>This class works as an interface between the database library/module and the bukkit application</p>
 * <p>It is responsible for authenticate users, register them and update any user data. </p>
 */
public class AuthManager {
    /**
     * Login method to authenticate the user. Refactored to use the GenericDAO class.
     *
     * @param username username of the user
     * @param password password of the user (NOT HASHED)
     * @return UserModel object if the user is authenticated, null otherwise
     */
    public static UserModel login(String username, String password) {
        password = Utils.hashString(password); // Hash the password
        try {
            // Create a new GenericDAO object to manipulate the UserModel class
            GenericDAO<UserModel> dao = new GenericDAO<>(DbConfig.getDbmsFactory(), UserModel.class);

            // Get the table name from the UserModel class
            String tableName = DbConfig.getValue(UserModel.class.getAnnotation(Table.class).key());

            // Get the username column name from the UserModel class. Found via annotations with the field key in the configuration file
            String usernameColumn = DbConfig.getValue(Arrays.stream(UserModel.class.getDeclaredFields())
                    .filter(field -> field.getName().equals("username"))
                    .findFirst()
                    .orElseThrow()
                    .getAnnotation(Column.class)
                    .key());

            // Get the password column name from the UserModel class. Found via annotations with the field key in the configuration file
            String passwordColumn = DbConfig.getValue(Arrays.stream(UserModel.class.getDeclaredFields())
                    .filter(field -> field.getName().equals("password"))
                    .findFirst()
                    .orElseThrow()
                    .getAnnotation(Column.class)
                    .key());

            // Perform the query to check if the user's credentials are correct. The query is built as 'SELECT * FROM users WHERE username = ? AND password = ?
            return dao.query(String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
                    tableName, usernameColumn, passwordColumn), username, password).stream().findAny().orElseThrow();
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during login - class {" + AuthManager.class.getName() + "}");
        }
        return null;
    }

    /**
     * Register a new user in the database. Refactored using the GenericDAO class.
     * @param userModel UserModel object to be registered (represents the player)
     * @return true if the user was successfully registered, false otherwise
     */
    public static boolean register(UserModel userModel) {
        try {
            GenericDAO<UserModel> dao = new GenericDAO<>(DbConfig.getDbmsFactory(), UserModel.class);
            return dao.insert(userModel) != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the UUID already exists in the database
     *
     * @param uuid UUID to be checked
     * @return true if the UUID already exists, false otherwise
     */
    private static boolean uuidExists(UUID uuid) {
        try {
            GenericDAO<UserModel> dao = new GenericDAO<>(DbConfig.getDbmsFactory(), UserModel.class);
            return dao.getById(uuid) != null;
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during uuidExists - class {" + AuthManager.class.getName() + "}");
        }
        return false;
    }

    /**
     * Check if the user is allowed to register, using the defined business rules
     * <p>Currently, it only checks the player's UUID</p>
     *
     * @param uuid UUID to be checked
     * @return false if the user is allowed to register, true otherwise
     */
    public static boolean isAllowedToRegister(UUID uuid) {
        return !uuidExists(uuid);
    }

    /**
     * Change the user's password in the database
     * @param uuid UUID of the user
     * @param password new password
     * @return true if the password was successfully changed, false otherwise
     */
    public static boolean changePassword(UUID uuid, String password) {
        try {
            GenericDAO<UserModel> dao = new GenericDAO<>(DbConfig.getDbmsFactory(), UserModel.class);
            UserModel userModel = dao.getById(uuid);
            userModel.setPassword(password);
            return dao.update(userModel) != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a user by its username
     * @param username username to be queried
     * @return UserModel object if the user exists, null otherwise
     */
    public static UserModel getUserByUsername(String username) {
        GenericDAO<UserModel> dao = new GenericDAO<>(DbConfig.getDbmsFactory(), UserModel.class);
        return dao.getByColumn("username", username).stream().findAny().orElse(null);
    }
}
