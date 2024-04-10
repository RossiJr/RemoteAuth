package com.rossijr.remoteauth.authentication;

import com.rossijr.remoteauth.authentication.dao.LoginDAO;
import com.rossijr.remoteauth.authentication.dao.RegisterDAO;
import com.rossijr.remoteauth.authentication.models.UserModel;

import java.sql.Connection;
import java.util.UUID;

/**
 * Authentication class
 * <p>
 * This class is responsible for the authentication methods of the user.
 */
public class Auth {
    /**
     * Login method to authenticate the user.
     * @param username
     * @param password
     * @param connection open connection to the database
     * @return UserModel object if the user is authenticated, null otherwise
     */
    public static UserModel login(String username, String password, Connection connection) {
        password = Utils.hashString(password);
        try{
            return LoginDAO.login(username, password, connection);
        }catch (Exception e){
            System.out.println("RemoteAuth --/ERROR/-- Error during login - class {" + Auth.class.getName() + "}");
        }
        return null;
    }

    /**
     * Register method to register the user.
     * @param uuid UUID of the user
     * @param username username of the user (unique identifier)
     * @param password password of the user
     * @param connection open connection to the database
     * @return
     */
    public static boolean register(UUID uuid, String username, String password, Connection connection) {
        password = Utils.hashString(password);
        try{
            return RegisterDAO.register(uuid, username, password, connection);
        }catch (Exception e){
            System.out.println("RemoteAuth --/ERROR/-- Error during register - class {" + Auth.class.getName() + "}");
        }
        return false;
    }

    /**
     * Check if the username already exists in the database
     * @param username username to be checked
     * @param connection open connection to the database
     * @return true if the username already exists, false otherwise
     */
    private static boolean usernameExists(String username, Connection connection) {
        try{
            return RegisterDAO.usernameExists(username, connection);
        }catch (Exception e){
            System.out.println("RemoteAuth --/ERROR/-- Error during usernameExists - class {" + Auth.class.getName() + "}");
        }
        return false;
    }

    /**
     * Check if the UUID already exists in the database
     * @param uuid UUID to be checked
     * @param connection open connection to the database
     * @return true if the UUID already exists, false otherwise
     */
    private static boolean uuidExists(UUID uuid, Connection connection) {
        try{
            return RegisterDAO.uuidExists(uuid, connection);
        }catch (Exception e){
            System.out.println("RemoteAuth --/ERROR/-- Error during uuidExists - class {" + Auth.class.getName() + "}");
        }
        return false;
    }

    /**
     * Check if the user is allowed to register
     * @param username username to be checked
     * @param connection open connection to the database
     * @return null if the user is allowed to register, a message explaining why the user is not allowed to register otherwise
     */
    public static String allowedToRegister(String username, Connection connection) {
        if(usernameExists(username, connection)){
            return "Username already exists";
        }
        return null;
    }

    /**
     * Check if the user is unregistered
     * @param uuid UUID of the user
     * @param username username of the user
     * @param connection open connection to the database
     * @return true if the user is unregistered, false otherwise
     */
    public static boolean isUnregistered(UUID uuid, String username, Connection connection) {
        return !usernameExists(username, connection) && !uuidExists(uuid, connection);
    }

}
