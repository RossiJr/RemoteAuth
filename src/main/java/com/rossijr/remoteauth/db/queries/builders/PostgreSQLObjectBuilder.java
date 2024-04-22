package com.rossijr.remoteauth.db.queries.builders;

import com.rossijr.remoteauth.db.annotations.model_annotation.Column;
import com.rossijr.remoteauth.db.config.DbConfig;
import com.rossijr.remoteauth.db.exceptions.IllegalColumnKeyException;
import com.rossijr.remoteauth.db.exceptions.ObjectBuildingException;
import com.rossijr.remoteauth.db.queries.ObjectBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostgreSQLObjectBuilder<T> implements ObjectBuilder<T> {
    private final Class<T> clazz;

    public PostgreSQLObjectBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }


    public T build(ResultSet resultSet) {
        T object;
        try {
            // Create a new instance of the object
            object = clazz.getDeclaredConstructor().newInstance();
            // Get all fields from the object
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = field.getName(); // Not permanent field
                try {
                    if (!field.getAnnotation(Column.class).key().isBlank()) { // Check if the key is not blank, if it is, don't change the column name to the given one
                        columnName = DbConfig.getValue(field.getAnnotation(Column.class).key()); // Get the column name from the configuration file
                        if (columnName == null){ // If the column name is not found in the configuration file, throw an exception
                            throw new IllegalColumnKeyException("Column key not found in configuration file");
                        }
                    }
                } catch (NullPointerException ignored) {
                } catch (IllegalColumnKeyException e) {
                    e.printStackTrace();
                }
                try {
                    Object value = resultSet.getObject(columnName);
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ObjectBuildingException("Error building object from ResultSet", e);
        }
        return object;
    }
}
