package com.rossijr.remoteauth.authentication;

import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.db.GenericDAO;
import com.rossijr.remoteauth.db.GenericDAOI;
import com.rossijr.remoteauth.db.exceptions.DatabaseNotSupportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * <p>This class works as an interface between the database library/module and the bukkit application</p>
 * <p>It is responsible for authenticate users, register them and update any user data. </p>
 */
public class AuthManager {
    /**
     * <p>GenericDAO object to interact with the database.</p>
     * <p>It is used to perform CRUD operations in the UserModel class</p>
     */
    private static final GenericDAOI<UserModel> userDao;
    private static final Logger logger = LogManager.getLogger(AuthManager.class);

    static {
        /*
         * Instantiation of the GenericDAO object.
         * In this case, dependency injection is not used because there's no need to.
         * It would overcomplicate the code, not representing an important gain.
         */
        userDao = new GenericDAO<>();
    }

    /**
     * Authenticate the user in the database with the provided UUID and password
     *
     * @param uuid     UUID of the user
     * @param password password of the user
     * @return UserModel object if the user was successfully authenticated, null otherwise
     */
    public static UserModel login(UUID uuid, String password) {
        password = hashString(password); // Hash the password
        try {
            UserModel userModel = userDao.getById(UserModel.class, uuid);
            // Check if the user exists and the password is correct
            if (userModel != null && userModel.getPassword().equals(password)) {
                logger.atInfo().log("[RemoteAuth] - LoginThread --o-o-- User {} successfully logged in", uuid);
                return userModel;
            }
        } catch (Exception e) {
            logger.atError().log("[RemoteAuth] - LoginThread --o-o-- Error during login. User: {}", uuid);
        }
        logger.atWarn().log("[RemoteAuth] - LoginThread --o-o-- User {} failed to login", uuid);
        return null;
    }

    /**
     * Register a new user in the database. Refactored using the GenericDAO class.
     *
     * @param userModel UserModel object to be registered (represents the player)
     * @return true if the user was successfully registered, false otherwise
     */
    public static boolean register(UserModel userModel) {
        try {
            userModel.setPassword(hashString(userModel.getPassword()));
            if (userDao.save(userModel) != null) {
                logger.atInfo().log("[RemoteAuth] - RegisterThread --o-o-- User {} successfully registered", userModel.getUuid());
                return true;
            } else {
                logger.atWarn().log("[RemoteAuth] - RegisterThread --o-o-- User {} didn't registered successfully", userModel.getUuid());
                return false;
            }
        } catch (Exception e) {
            logger.atError().log("[RemoteAuth] - RegisterThread --o-o-- Error during register. User: {}", userModel.getUuid());
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
            logger.atError().log("[RemoteAuth] - SecondaryThreads --o-o-- Error during UUID check. UUID: {}", uuid);
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
     *
     * @param uuid     UUID of the user
     * @param password new password
     * @return true if the password was successfully changed, false otherwise
     */
    public static boolean changePassword(UUID uuid, String password) {
        try {
            UserModel userModel = userDao.getById(UserModel.class, uuid);
            userModel.setPassword(hashString(password));
            if (userDao.update(userModel) != null) {
                logger.atInfo().log("[RemoteAuth] - PasswdChangeThread --o-o-- User {} successfully changed password", uuid);
                return true;
            } else {
                logger.atWarn().log("[RemoteAuth] - PasswdChangeThread --o-o-- User {} didn't change password successfully", uuid);
                return false;
            }
        } catch (Exception e) {
            logger.atError().log("[RemoteAuth] - PasswdChangeThread --o-o-- Error during change password. User: {}", uuid);
            return false;
        }
    }

    /**
     * Get a user by its username
     *
     * @param username username to be queried
     * @return UserModel object if the user exists, null otherwise
     */
    public static UserModel getUserByUsername(String username) {
        logger.atInfo().log("[RemoteAuth] - SecondaryThreads --o-o-- Searching for user with username \"{}\"", username);
        return (UserModel) userDao.getByColumn(UserModel.class, "username", username).stream().findFirst().orElse(null);
    }


    /**
     * Hashes a string using the SHA-256 algorithm
     *
     * @param string string to be hashed
     * @return hashed string
     */
    public static String hashString(String string) {
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
            logger.atError().log("RA - SecondaryThreads --o-o-- Error during hashing");
            return null;
        }
    }
}
