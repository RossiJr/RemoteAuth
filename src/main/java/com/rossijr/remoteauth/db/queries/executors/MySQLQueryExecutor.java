package com.rossijr.remoteauth.db.queries.executors;

import com.rossijr.remoteauth.db.Utils;
import com.rossijr.remoteauth.db.annotations.model_annotation.HashedField;
import com.rossijr.remoteauth.db.annotations.model_annotation.Id;
import com.rossijr.remoteauth.db.exceptions.IdFieldMissingException;
import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.builders.MySQLObjectBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class MySQLQueryExecutor<T> implements QueryExecutor<T> {
    @Override
    public Object executeInsertQuery(String query, Object object, Connection connection) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        for (int i = 0; i < object.getClass().getDeclaredFields().length; i++) {
            Field f = object.getClass().getDeclaredFields()[i];
            try {
                f.setAccessible(true);

                if(f.isAnnotationPresent(HashedField.class) && f.getType().equals(String.class)) {
                    f.set(object, Utils.hashString(String.valueOf(f.get(object))));
                }

                if (f.getType().equals(UUID.class)) {
                    pstmt.setObject(i + 1, f.get(object).toString());
                } else {
                    pstmt.setObject(i + 1, f.get(object));
                }

            } catch (IllegalAccessException | RuntimeException e) {
               throw new SQLException(e.getMessage());
            }
        }
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getObject(1);
        } else {
            try {
                Field idField = Arrays.stream(object.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
                idField.setAccessible(true);
                return idField.get(object);
            } catch (IllegalAccessException e) {
                throw new IdFieldMissingException("Id field not found in the object");
            }

        }
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
     * @param tClass Class of the object to be returned
     * @param connection Opened connection to the database
     * @param params Parameters to be set in the prepared statement
     * @return List of objects with the result of the query
     * @throws SQLException If an error occurs during the query execution
     */
    @Override
    public Object executeSelectQuery(String query, Class<T> tClass, Connection connection, Object... params) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i=0; i<params.length; i++) {
            pstmt.setObject(i+1, params[i] instanceof UUID ? params[i].toString() : params[i]);
        }
        var rs = pstmt.executeQuery();
        List<T> list = new ArrayList<>();
        while(rs.next()) {
            MySQLObjectBuilder<T> objectBuilder = new MySQLObjectBuilder<>(tClass);
            list.add(objectBuilder.build(rs));
        }
        return list;
    }

    @Override
    public Object executeUpdateQuery(String query, Object object, Connection connection) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        Object id = null;
        for (Field f : object.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (!f.isAnnotationPresent(Id.class)) {
                    if (f.isAnnotationPresent(HashedField.class) && f.getType().equals(String.class)) {
                        try {
                            f.set(object, Utils.hashString(String.valueOf(f.get(object))));
                        } catch (IllegalAccessException e) {
                            throw new SQLException(e.getMessage());
                        }
                    }
                    if (f.getType().equals(UUID.class)) {
                        pstmt.setObject(i++, f.get(object).toString());
                    } else {
                        pstmt.setObject(i++, f.get(object));
                    }
                } else {
                    id = f.get(object);
                }
            } catch (IllegalAccessException e) {
                throw new SQLException(e.getMessage());
            }
        }
        pstmt.setObject(i, id);
        pstmt.executeUpdate();
        return object;
    }

}
