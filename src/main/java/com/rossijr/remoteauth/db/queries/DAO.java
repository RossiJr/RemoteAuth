package com.rossijr.remoteauth.db.queries;

import java.sql.SQLException;

public interface DAO<T> {
    T getById(Object id) throws SQLException;
    T getByColumn(String column, Object value);
    T insert(T object);
    T update(T object);
    T query(String query, Object... params) throws SQLException;
}
