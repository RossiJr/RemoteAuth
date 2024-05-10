package com.rossijr.remoteauth.authentication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.db.GenericDAOI;
import com.rossijr.remoteauth.db.dependencyinjection.AppModule;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * <p>This class works as an interface between the database library/module and the bukkit application</p>
 * <p>It is responsible for authenticate users, register them and update any user data. </p>
 */
public class AuthManager {
    private static GenericDAOI<UserModel> userDao;
    /**
     * Login method to authenticate the user. Refactored to use the GenericDAO class.
     *
     * @param username username of the user
     * @param password password of the user (NOT HASHED)
     * @return UserModel object if the user is authenticated, null otherwise
     */

    static {
        Injector injector = Guice.createInjector(new AppModule());

        // Specify the class you want to inject
        injector.getInstance(AppModule.class).bindDAO(UserModel.class);

        // Injecting the DAO
        userDao = injector.getInstance(new Key<GenericDAOI<UserModel>>() {});
    }

    public static UserModel login(String username, String password) {
        password = hashString(password); // Hash the password
        try {
            UserModel userModel = (UserModel) userDao.getByColumn(UserModel.class, "username", username).stream().findAny().orElse(null);
            if (userModel != null && userModel.getPassword().equals(password)) {
                return userModel;
            }
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
            return userDao.save(userModel) != null;
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during register - class {" + AuthManager.class.getName() + "}");
            return false;
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
            return userDao.getById(UserModel.class, uuid) != null;
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
            UserModel userModel = userDao.getById(UserModel.class, uuid);
            userModel.setPassword(password);
            return userDao.update(userModel) != null;
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during changePassword - class {" + AuthManager.class.getName() + "}");
            return false;
        }
    }

    /**
     * Get a user by its username
     * @param username username to be queried
     * @return UserModel object if the user exists, null otherwise
     */
    public static UserModel getUserByUsername(String username) {
        return (UserModel) userDao.getByColumn(UserModel.class, "username", username).stream().findAny().orElse(null);
    }


    public static String hashString(String string){
        try {
            // Configure the hash algorithm to be used
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            // Hash the string with UTF-8 encoding
            byte[] messageDigest = algorithm.digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                // Convert the byte to hexadecimal to string | 0xff & b -> mask to obtain only the 8 most significant bits
                String hex = Integer.toHexString(0xff & b);
                // If the length hex is equal to 1, append 0 to the string
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during hashing - class {" + AuthManager.class.getName() + "}");
            return null;

        }
    }
}
