package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.authentication.models.UserModel;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Properties;

public class Configuration {
    private static Properties properties;
    private static SessionFactory sessionFactory;

    public static void init(Properties properties){
        try {
            Configuration.properties = properties;

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(properties)
                    .build();
            MetadataSources sources = new MetadataSources(registry);

            // Add annotated classes (entities)
            sources.addAnnotatedClass(UserModel.class);

            sessionFactory = sources.buildMetadata().buildSessionFactory();

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void setSessionFactory(SessionFactory sessionFactory) {
        Configuration.sessionFactory = sessionFactory;
    }
}
