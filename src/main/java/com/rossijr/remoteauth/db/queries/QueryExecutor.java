package com.rossijr.remoteauth.db.queries;

import java.sql.Connection;
import java.sql.SQLException;

public interface QueryExecutor<T> {
    Object executeInsertQuery(String query, Object object, Connection connection) throws SQLException;
    Object executeSelectQueryByID(String query, Object id, Class<T> tClass, Connection connection) throws SQLException;
    Object executeSelectQuery(String query, Class<T> tClass, Connection connection, Object... params) throws SQLException;
    Object executeUpdateQuery(String query, Object object, Connection connection) throws SQLException;
}
