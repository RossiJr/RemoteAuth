package com.rossijr.remoteauth.db.queries.executors;

import com.rossijr.remoteauth.db.Utils;
import com.rossijr.remoteauth.db.annotations.model_annotation.HashedField;
import com.rossijr.remoteauth.db.annotations.model_annotation.Id;
import com.rossijr.remoteauth.db.exceptions.IdFieldMissingException;
import com.rossijr.remoteauth.db.queries.ObjectBuilder;
import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.builders.PostgreSQLObjectBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostgreSQLQueryExecutor<T> implements QueryExecutor<T> {

    @Override
    public Object executeInsertQuery(String query, Object object, Connection connection) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        for (int i = 0; i < object.getClass().getDeclaredFields().length; i++) {
            Field f = object.getClass().getDeclaredFields()[i];
            try {
                f.setAccessible(true);

                if (f.isAnnotationPresent(HashedField.class) && f.getType().equals(String.class)) {
                    f.set(object, Utils.hashString(String.valueOf(f.get(object))));
                }

                pstmt.setObject(i + 1, f.get(object));

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
        pstmt.setObject(1, id);
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            ObjectBuilder<T> objectBuilder = new PostgreSQLObjectBuilder<>(tClass);
            object = objectBuilder.build(rs);
            return object;
        }
        return null;
    }

    @Override
    public Object executeSelectQuery(String query, Class<T> tClass, Connection connection, Object... params) throws SQLException {
        var pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        var rs = pstmt.executeQuery();
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            ObjectBuilder<T> objectBuilder = new PostgreSQLObjectBuilder<>(tClass);
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
                    pstmt.setObject(i++, f.get(object));
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