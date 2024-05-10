package com.rossijr.remoteauth.db;

import java.util.List;

public interface GenericDAOI<T> {
    T save(T entity);
    T update(T entity);
    boolean delete(T entity);
    T getById(Class<T> clazz, Object id);
    List getByColumn(Class<T> clazz, String column, String value);
}
