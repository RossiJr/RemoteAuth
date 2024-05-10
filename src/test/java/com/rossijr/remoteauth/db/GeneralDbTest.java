package com.rossijr.remoteauth.db;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.db.dependencyinjection.AppModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class GeneralDbTest {
    private static final Logger logger = LogManager.getLogger(GeneralDbTest.class);
    private static SessionFactory sessionFactory;

    @Inject
    private static GenericDAOI<UserModel> userDao;

    @BeforeAll
    public static void setUp() {
//        logger.atInfo().log("Setting up test");
//        Injector injector = Guice.createInjector(new TestModule());
//        logger.atInfo().log("Injector created");
//        sessionFactory = injector.getInstance(SessionFactory.class);
//        logger.atInfo().log("Session factory created");
//        userDao = injector.getInstance(new Key<GenericDAOI<UserModel>>() {});
    }


    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testHibernateOperations() {
        logger.atInfo().log("Testing hibernate operations");
        UserModel newUser = new UserModel(UUID.randomUUID(), "user3", "password3");
        UserModel newUser2 = new UserModel(UUID.randomUUID(), "user4", "password3");

        logger.atInfo().log("Saving new users");
        assertEquals(userDao.save(newUser).getUuid(), newUser.getUuid());
        assertEquals(userDao.save(newUser2).getUuid(), newUser2.getUuid());
        logger.atInfo().log("Users saved");
    }

}
