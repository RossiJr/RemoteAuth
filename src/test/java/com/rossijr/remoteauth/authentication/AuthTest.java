package com.rossijr.remoteauth.authentication;

import com.rossijr.remoteauth.authentication.models.UserModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.logging.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTest {

    private final Logger logger = Logger.getLogger(AuthTest.class.getName());


    @Test
    public void testLoginValidCredentials() {
        logger.info("Testing login with valid credentials");
        // Mocked credentials
        String username = "testUser";
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(UUID.randomUUID(), username, Utils.hashString(password)));

            // Perform the login
            assertNotNull(Auth.login(username, password, connection));
            logger.info("Valid credentials login test successful");

        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during connection - class {" + AuthTest.class.getName() + "}");
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginInvalidCredentials() {
        logger.info("Testing login with invalid credentials");
        // Mocked credentials
        String username = "testUser";
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(UUID.randomUUID(), username, Utils.hashString(password)));

            logger.info("Performing the login");
            // Perform the login
            assertNull(Auth.login(username, "wrongPassword", connection));
            logger.info("Invalid credentials login test successful");

        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during connection - class {" + AuthTest.class.getName() + "}");
            e.printStackTrace();
        }
    }

    @Test
    public void testValidRegister() {
        logger.info("Testing register with valid credentials");
        // Mocked credentials
        String username = "testUser";
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(UUID.randomUUID(), username, Utils.hashString(password)));

            // Perform the register
            assertTrue(Auth.register(java.util.UUID.randomUUID(), "newUser", "newPassword", connection));
            logger.info("Valid credentials register test successful");

        } catch (Exception e) {
            logger.severe("Error during connection");
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidRegister() {
        logger.info("Testing register with invalid credentials");
        // Mocked credentials
        String username = "testUser";
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(UUID.randomUUID(), username, Utils.hashString(password)));

            logger.info("Performing the register");
            // Perform the register
            assertFalse(Auth.register(java.util.UUID.randomUUID(), "testUser", "testPassword", connection));

            UUID randomUUID = UUID.randomUUID();
            assertTrue(Auth.register(randomUUID, "newUser", "newPassword", connection));
            assertFalse(Auth.register(randomUUID, "newUser", "newPassword", connection));
            logger.info("Invalid credentials register test successful");

        } catch (Exception e) {
            logger.severe("Error during connection");
            e.printStackTrace();
        }
    }

    @Test
    public void testIsUnregistered() {
        logger.info("Testing isUnregistered with valid and invalid credentials");
        // Mocked credentials
        UUID randomUUID = UUID.randomUUID();
        String username = "testUser";
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(randomUUID, username, Utils.hashString(password)));

            logger.info("Performing the isUnregistered");
            // Perform the isUnregistered
            assertFalse(Auth.isUnregistered(randomUUID, username, connection));
            assertFalse(Auth.isUnregistered(randomUUID, "newUser", connection));
            assertFalse(Auth.isUnregistered(UUID.randomUUID(), username, connection));
            assertTrue(Auth.isUnregistered(UUID.randomUUID(), "newUser", connection));
            logger.info("isUnregistered test successful");

        } catch (Exception e) {
            logger.severe("Error during connection");
            e.printStackTrace();
        }
    }

    @Test
    public void testAllowedToRegister() {
        logger.info("Testing allowedToRegister with valid and invalid credentials");
        // Mocked credentials
        String username = "testUser";
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(UUID.randomUUID(), username, Utils.hashString(password)));

            logger.info("Performing the isAllowedToRegister");
            // Perform the isAllowedToRegister
            assertEquals("Username already exists", Auth.allowedToRegister(username, connection));
            assertNull(Auth.allowedToRegister("newUser", connection));
            logger.info("allowedToRegister test successful");

        } catch (Exception e) {
            logger.severe("Error during connection");
            e.printStackTrace();
        }
    }
}
