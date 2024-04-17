package com.rossijr.remoteauth.authentication;

import com.rossijr.remoteauth.authentication.models.UserModel;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ChangePasswordTest {
    private final Logger logger = Logger.getLogger(ChangePasswordTest.class.getName());

    @Test
    public void testChangePassword() {
        logger.info("Testing changing password");
        // Mocked credentials
        UUID user = UUID.randomUUID();
        String password = "testPassword";

        // Mocked connection
        try {
            logger.info("Initializing connection to the database and creating the user table");
            Connection connection = AuthTestUtils.mockConnection();
            AuthTestUtils.databaseInitiation(connection, new UserModel(user, "test", Utils.hashString(password)));

            // Perform the login
            assertTrue(Auth.changePassword(user, "newPassword", connection));
            assertNotNull(Auth.login("test", "newPassword", connection));

            logger.info("Changing password test successful");
        } catch (Exception e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during connection - class {" + AuthTest.class.getName() + "}");
            e.printStackTrace();
        }
    }

}
