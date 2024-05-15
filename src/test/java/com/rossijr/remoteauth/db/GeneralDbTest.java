package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.authentication.AuthManager;
import com.rossijr.remoteauth.authentication.models.UserModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.jdo.JDOHelper;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralDbTest {
    private static final Logger logger = LogManager.getLogger(GeneralDbTest.class);
    private static GenericDAOI<UserModel> userDao;

    private static Properties defineHibernateDatabaseParameters(){
        Properties properties = new Properties();
        properties.setProperty(Environment.JAKARTA_JDBC_DRIVER, "org.h2.Driver");
        properties.setProperty(Environment.JAKARTA_JDBC_URL, "jdbc:h2:mem:TEST;DB_CLOSE_DELAY=-1");
        properties.setProperty(Environment.JAKARTA_JDBC_USER, "sa");
        properties.setProperty(Environment.JAKARTA_JDBC_PASSWORD, "");
        properties.setProperty(Environment.JAKARTA_HBM2DDL_DB_NAME, "create-drop");
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }

    private static Properties defineNoSQLDatabaseParameters(){
        Properties properties = new Properties();
        properties.setProperty("db.type", "nosql");
        properties.setProperty("javax.jdo.option.ConnectionURL", "mongodb://localhost:27017/RemoteAuth");
        properties.setProperty("javax.jdo.option.Mapping", "mongodb");
        properties.setProperty("datanucleus.metadata.allowLoadAtRuntime", "true");
        properties.setProperty("datanucleus.autoCreateSchema", "true");
        return properties;
    }

    private static void createSchema(){
        logger.atInfo().log("Creating schema");
        try (Session session = Configuration.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE TABLE users (uuid UUID PRIMARY KEY, " +
                            "username VARCHAR(255) NOT NULL UNIQUE, " +
                            "password VARCHAR(255) NOT NULL)")
                    .executeUpdate();
        } catch (HibernateException e) {
            throw new RuntimeException("Error during table creation - Hibernate", e);
        }
        logger.atInfo().log("Schema created");
    }

    @BeforeAll
    public static void setUp() {
        logger.atInfo().log("Setting up test environment");
        Configuration.init(defineHibernateDatabaseParameters());
        logger.atInfo().log("Test environment set up");

        createSchema();

        logger.atInfo().log("Setting up GenericDAO");
        userDao = new GenericDAO<>();
        logger.atInfo().log("GenericDAO instantiated");

    }

    @Test
    public void testHibernateOperations() {
        logger.atInfo().log("Testing hibernate operations");
        UserModel newUser = new UserModel(UUID.randomUUID(), "user3", "password3");
        UserModel newUser2 = new UserModel(UUID.randomUUID(), "user4", "password3");

        logger.atInfo().log("Saving new users");
        assertEquals(newUser.getUuid(), userDao.save(newUser).getUuid());
        assertEquals(newUser2.getUuid(), userDao.save(newUser2).getUuid());
        logger.atInfo().log("Users saved");
    }

    @Test
    public void testAuthManagerRegister(){
        logger.atInfo().log("Testing AuthManager register");
        UserModel newUser = new UserModel(UUID.randomUUID(), "user5", "password5");
        assertTrue(AuthManager.register(newUser));
        logger.atInfo().log("AuthManager register test passed");
    }

    @Test
    public void testAuthManagerLogin() {
        logger.atInfo().log("Testing AuthManager login");
        UUID userUUID = UUID.randomUUID();
        UserModel newUserWithHash = new UserModel(userUUID, "user6", AuthManager.hashString("password6"));

        logger.atInfo().log("Saving new user with hashed password");
        userDao.save(newUserWithHash);
        try {
            logger.atInfo().log("Testing login with correct password");
            assertEquals(AuthManager.login(userUUID, "password6").getUuid(), userUUID);
            logger.atInfo().log("Login test passed");

            logger.atInfo().log("Testing login with incorrect password");
            assertNull(AuthManager.login(userUUID, "password7"));
            logger.atInfo().log("Login test passed");
        } catch (NullPointerException e){
            logger.atError().log("Error during login test");
            fail("Error during login test");
        }
    }

    @Test
    public void testChangePassword(){
        logger.atInfo().log("Testing change password");
        logger.atInfo().log("Defining user with hashed password");
        UUID userUUID = UUID.randomUUID();
        UserModel newUser = new UserModel(userUUID, "user7", AuthManager.hashString("password7"));
        userDao.save(newUser);
        logger.atInfo().log("User saved");

        logger.atInfo().log("Changing password");
        assertTrue(AuthManager.changePassword(userUUID, "password8"));
        logger.atInfo().log("Password changed");

        logger.atInfo().log("Testing login with new password");
        assertEquals(AuthManager.login(userUUID, "password8").getUuid(), userUUID);
        logger.atInfo().log("Login with new password test passed");
    }

    @Test
    public void testGetUserByUsername() {
        logger.atInfo().log("Testing get user by username");
        UUID userUUID = UUID.randomUUID();
        UserModel newUser = new UserModel(userUUID, "user8", AuthManager.hashString("password8"));
        userDao.save(newUser);

        logger.atInfo().log("Testing get user by username with existing user");
        assertEquals(userUUID, AuthManager.getUserByUsername("user8").getUuid());
        logger.atInfo().log("Get user by username test passed");

        logger.atInfo().log("Testing get user by username with non-existing user");
        assertNull(AuthManager.getUserByUsername("user9"));
        logger.atInfo().log("Get user by username test passed");
    }

    @Test
    public void testNoSQLDatabase() {
        logger.atInfo().log("Setting up NoSQL database");
        Configuration.setPmf(JDOHelper.getPersistenceManagerFactory(defineNoSQLDatabaseParameters()));
        NoSQLDao<UserModel> newDao = new NoSQLDao<>();
        logger.atInfo().log("NoSQL database set up");

        logger.atInfo().log("Testing NoSQL database");
        UserModel newUser = new UserModel(UUID.randomUUID(), "user9", "password9");
        UserModel newUser2 = new UserModel(UUID.randomUUID(), "user10", "password10");

        logger.atInfo().log("Saving new users");
        assertEquals(newUser.getUuid(), newDao.save(newUser).getUuid());
        assertEquals(newUser2.getUuid(), newDao.save(newUser2).getUuid());
        logger.atInfo().log("Users saved");

        logger.atInfo().log("Testing get by column");
        assertEquals(1, newDao.getByColumn(UserModel.class, "username", "user9").size());
        assertEquals(1, newDao.getByColumn(UserModel.class, "username", "user10").size());
        logger.atInfo().log("Get by column test passed");
    }
}
