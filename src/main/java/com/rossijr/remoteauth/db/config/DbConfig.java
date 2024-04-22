package com.rossijr.remoteauth.db.config;


import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.db.queries.QueryFactory;
import com.rossijr.remoteauth.db.queries.factories.MySQLQueryFactory;
import com.rossijr.remoteauth.db.queries.factories.PostgreSQLQueryFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    /**
     * Properties loaded from db.properties file
     */
    private static Properties properties = new Properties();
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
            String dbms = properties.getProperty("db.dbms");
            if (dbms == null || dbms.isBlank()) {
                throw new RuntimeException("Database management system not defined in db.properties");
            }

            // Loads the database's driver
            switch (dbms) {
                case "mysql":
                    bdStartup("com.mysql.cj.jdbc.Driver");
                    break;
                case "postgresql":
                    bdStartup("org.postgresql.Driver");
                    break;
                default:
                    throw new RuntimeException("Database management system not supported");
            }
        } catch (Exception e) {
            properties.clear();
        }
    }

    /**
     * Get the database URL, replacing the placeholders with the values from the properties file
     *
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
     *
     * @return Database username
     */
    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    /**
     * Get the database password
     *
     * @return Database password
     */
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }

    /**
     * Get the user schema
     *
     * @return User schema
     */
    public static String getUserSchema() {
        String schema = properties.getProperty("db.user_schema");
        // Return the schema if it is not null, otherwise return "public"
        return schema != null ? schema : "public";
    }

    /**
     * Get the user table
     *
     * @return User table
     */
    public static String getUserTable() {
        String table = properties.getProperty("db.user_table");
        return table != null ? table : "users";
    }

    /**
     * Get the username column
     *
     * @return Username column
     */
    public static String getUsernameColumn() {
        String column = properties.getProperty("db.username_column");
        return column != null ? column : "username";
    }

    /**
     * Get the password column
     *
     * @return Password column
     */
    public static String getPasswordColumn() {
        String column = properties.getProperty("db.password_column");
        return column != null ? column : "password";
    }

    /**
     * Get the UUID column
     *
     * @return UUID column
     */
    public static String getUUIDColumn() {
        String column = properties.getProperty("db.uuid_column");
        return column != null ? column : "uuid";
    }

    public static boolean isDbConfigured() {
        return !properties.isEmpty();
    }

    public static void bdStartup(String driverClassName) throws RuntimeException {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading database driver", e);
        }
    }

    public static void setProperties(Properties properties) {
        DbConfig.properties = properties;
    }

    /**
     * Get the value of a key from the properties file
     * @param key Key to get the value from
     * @return Value of the key
     */
    public static String getValue(String key){
        switch (key){
            case "db.dbms":
                return properties.getProperty("db.dbms");
            case "db.host":
                return properties.getProperty("db.host");
            case "db.port":
                return properties.getProperty("db.port");
            case "db.database":
                return properties.getProperty("db.database");
            case "db.user_table":
                return getUserTable();
            case "db.uuid_column":
                return getUUIDColumn();
            case "db.username_column":
                return getUsernameColumn();
            case "db.password_column":
                return getPasswordColumn();
            case "db.username":
                return getDbUsername();
            case "db.password":
                return getDbPassword();
            default:
                return properties.getProperty(key);
        }
    }

    public static QueryFactory getDbmsFactory(){
        QueryFactory factory;
        String dbms = properties.getProperty("db.dbms");
        if(dbms.equals("mysql"))
            factory = new MySQLQueryFactory<>();
        else if (dbms.equals("postgresql"))
            factory = new PostgreSQLQueryFactory<>();
        else
            throw new RuntimeException("Database management system not supported");

        return factory;
    }
}
