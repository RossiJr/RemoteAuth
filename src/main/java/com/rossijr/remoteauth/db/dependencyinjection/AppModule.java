package com.rossijr.remoteauth.db.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.rossijr.remoteauth.db.GenericDAO;
import com.rossijr.remoteauth.db.GenericDAOI;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        // No binding needed for the GenericDAOImpl, as Guice will handle it automatically
    }

    // Add a method to bind the generic provider for any given class
    public <T> void bindDAO(Class<T> clazz) {
        bind(new TypeLiteral<GenericDAOI<T>>() {}).toProvider(new DAOProvider<>(clazz, getProvider(Injector.class).get()));
    }
}
