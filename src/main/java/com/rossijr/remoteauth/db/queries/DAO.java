package com.rossijr.remoteauth.db.queries;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T getById(Object id) throws SQLException;
    List<T> getByColumn(String column, Object value);
    Object insert(T object) throws SQLException;
    T update(T object) throws SQLException;
    List<T> query(String query, Object... params) throws SQLException;
}
