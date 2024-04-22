package com.rossijr.remoteauth.db.queries.executors;

import com.rossijr.remoteauth.db.queries.builders.MySQLObjectBuilder;
import com.rossijr.remoteauth.db.queries.QueryExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


public class MySQLQueryExecutor<T> implements QueryExecutor<T> {
    @Override
    public Object executeInsertQuery(String query) {
        return null;
    }

    @Override
    public Object executeSelectQueryByID(String query, Object id, Class<T> tClass, Connection connection) throws SQLException {
        T object;
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setObject(1, (id instanceof UUID ? id.toString() : id));
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            MySQLObjectBuilder<T> objectBuilder = new MySQLObjectBuilder<>(tClass);
            object = objectBuilder.build(rs);
            return object;
        }
        return null;
    }

    /**
     * Execute a select query with prepared statement and return the object
     * @param query Query to execute
     * @param tClass
     * @param connection
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public Object executeSelectQuery(String query, Class<T> tClass, Connection connection, Object... params) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i=0; i<params.length; i++) {
            pstmt.setObject(i+1, params[i] instanceof UUID ? params[i].toString() : params[i]);
        }
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            MySQLObjectBuilder<T> objectBuilder = new MySQLObjectBuilder<>(tClass);
            return objectBuilder.build(rs);
        }
        return null;
    }

}
