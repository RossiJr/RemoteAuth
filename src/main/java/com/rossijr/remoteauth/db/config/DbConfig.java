package com.rossijr.remoteauth.db.config;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;

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
        StringBuilder propertiesPath = new StringBuilder();
        propertiesPath.append(System.getProperty("user.dir"));
        propertiesPath.append(File.separator);
        propertiesPath.append("db.properties");

        // Load the properties file into the properties object
        try (InputStream input = new FileInputStream(propertiesPath.toString())) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
}
