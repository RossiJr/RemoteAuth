package com.rossijr.remoteauth.db.queries.executors;

import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.builders.MySQLObjectBuilder;
import com.rossijr.remoteauth.db.queries.builders.PostgreSQLObjectBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class PostgreSQLQueryExecutor<T> implements QueryExecutor<T> {
    @Override
    public Object executeInsertQuery(String query) {
        return null;
    }

    @Override
    public Object executeSelectQueryByID(String query, Object id, Class<T> tClass, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public Object executeSelectQuery(String query, Class<T> tClass, Connection connection, Object... params) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i=0; i<params.length; i++) {
            pstmt.setObject(i+1, params[i]);
        }
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            PostgreSQLObjectBuilder<T> objectBuilder = new PostgreSQLObjectBuilder<>(tClass);
            return objectBuilder.build(rs);
        }
        return null;
    }
}