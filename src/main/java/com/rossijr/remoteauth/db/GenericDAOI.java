package com.rossijr.remoteauth.db;

import java.util.List;

public interface GenericDAOI<T> {
    T save(T entity);
    T update(T entity);
    boolean delete(T entity);
    T getById(Class<T> clazz, Object id);
    List<T> getByColumn(Class<T> clazz, String column, String value);
}
