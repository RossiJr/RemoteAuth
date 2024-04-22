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

/**
 * Object builder for MySQL databases
 * @param <T> Object type
 */
public class MySQLObjectBuilder<T> implements ObjectBuilder<T> {
    private final Class<T> clazz;

    public MySQLObjectBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Build an object from a ResultSet
     * @param resultSet ResultSet to build the object from
     * @return Object built from the ResultSet
     */
    public T build(ResultSet resultSet) {
        T object;
        try {
            // Create a new instance of the object
            object = clazz.getDeclaredConstructor().newInstance();
            // Get all fields from the object
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = field.getName();
                try {
                    if (!field.getAnnotation(Column.class).key().isBlank()) { // Check if the key is not blank, if it is, don't change the column name to the given one
                        columnName = DbConfig.getValue(field.getAnnotation(Column.class).key()); // Get the column name from the configuration file
                        if (columnName == null){
                            throw new IllegalColumnKeyException("Column key not found in configuration file");
                        }
                    }
                } catch (NullPointerException ignored) {
                } catch (IllegalColumnKeyException e) {
                    e.printStackTrace();
                }
                try {
                    // Set the field value from the ResultSet
                    Object value = resultSet.getObject(columnName);

                    // Convert UUID bytes to UUID in case the field is a UUID
                    if(field.getType().equals(UUID.class)){
                        byte[] uuidBytes = (byte[]) value;
                        value = bytesToUUID(uuidBytes);
                    }
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

    private UUID bytesToUUID(byte[] bytes) {
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (bytes[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        return new UUID(msb, lsb);
    }

}
