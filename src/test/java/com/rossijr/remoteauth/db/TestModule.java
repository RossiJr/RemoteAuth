package com.rossijr.remoteauth.db;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.rossijr.remoteauth.authentication.models.UserModel;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestModule extends AbstractModule {
    @Provides
    public SessionFactory provideSessionFactory() {
        return new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .addAnnotatedClass(UserModel.class) // Add annotated class (entity)
                .buildSessionFactory();
    }

    @Override
    protected void configure() {
        bind(GenericDAOI.class).to(GenericDAO.class);
    }
}
