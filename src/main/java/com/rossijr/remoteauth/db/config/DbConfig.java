package com.rossijr.remoteauth.db.config;


import com.rossijr.remoteauth.config.Settings;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    /**
     * Properties loaded from db.properties file
     */
    private static final Properties properties = new Properties();
    /**
     * Database template URL
     */
    private static final String url = "jdbc:{bdms}://{host}:{port}/{database}";

    static {
        // Generate the path to the db.properties file
        String propertiesPath = Settings.getDbFilePath();

        // Load the properties file into the properties object
        try (InputStream input = new FileInputStream(propertiesPath)) {
            properties.load(input);
        } catch (Exception e) {
            properties.clear();
        }
    }

    /**
     * Get the database URL, replacing the placeholders with the values from the properties file
     * @return Database URL
     */
    public static String getDbUrl() {
        return url.replace("{bdms}", properties.getProperty("db.dbms"))
                .replace("{host}", properties.getProperty("db.host"))
                .replace("{port}", properties.getProperty("db.port"))
                .replace("{database}", properties.getProperty("db.database"));
    }

    /**
     * Get the database username
     * @return Database username
     */
    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    /**
     * Get the database password
     * @return Database password
     */
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }

    /**
     * Get the user schema
     * @return User schema
     */
    public static String getUserSchema(){
        String schema = properties.getProperty("db.user_schema");
        // Return the schema if it is not null, otherwise return "public"
        return schema != null ? schema : "public";
    }

    /**
     * Get the user table
     * @return User table
     */
    public static String getUserTable(){
        String table = properties.getProperty("db.user_table");
        return table != null ? table : "users";
    }

    /**
     * Get the username column
     * @return Username column
     */
    public static String getUsernameColumn(){
        String column = properties.getProperty("db.username_column");
        return column != null ? column : "username";
    }

    /**
     * Get the password column
     * @return Password column
     */
    public static String getPasswordColumn(){
        String column = properties.getProperty("db.password_column");
        return column != null ? column : "password";
    }

    /**
     * Get the UUID column
     * @return UUID column
     */
    public static String getUUIDColumn(){
        String column = properties.getProperty("db.uuid_column");
        return column != null ? column : "uuid";
    }

    public static boolean isDbConfigured() {
        return !properties.isEmpty();
    }

}
