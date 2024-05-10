package com.rossijr.remoteauth.db.dependencyinjection;

import com.google.inject.*;
import com.rossijr.remoteauth.db.GenericDAOI;

public class DAOProvider<T> implements Provider<GenericDAOI<T>> {
    private final Class<T> clazz;
    private final Injector injector;

    @Inject
    public DAOProvider(Class<T> clazz, Injector injector) {
        this.clazz = clazz;
        this.injector = injector;
    }

    @Override
    public GenericDAOI<T> get() {
        return injector.getInstance(Key.get(new TypeLiteral<GenericDAOI<T>>() {}));
    }
}
