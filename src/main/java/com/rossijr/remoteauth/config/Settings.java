package com.rossijr.remoteauth.config;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.config.messages.Parameters;
import com.rossijr.remoteauth.db.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.hibernate.cfg.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

/**
 * Settings class design to store the plugin settings
 */
public class Settings {
    private static final String fileSeparator = File.separator;

    private static final String serverDirectory = System.getProperty("user.dir");
    private static final String pluginName = "RemoteAuth";

    private static Properties dbProperties;

    private static final String messagesFileName = "messages.properties";

    private static HashMap<String, String> messages = new HashMap<>();

    /**
     * Check if any message was loaded, used to optimize the message loading
     */
    private static boolean anyMessageLoaded = false;

    /**
     * Method to initialize specific plugin settings and configurations
     * @param config FileConfiguration object with the plugin configurations
     */
    public static void init(FileConfiguration config) throws NullPointerException {
        // Loads the messages
        generateMessages();

        // Load the database configurations
        Configuration.init(loadBdConfigurations(config));

        // Load the log configurations
        LogConfigurator.init(loadLogConfigurations(config));
    }

    private static Properties loadLogConfigurations(FileConfiguration configuration) {
        Properties properties = new Properties();
        properties.setProperty("log.level", configuration.getString("log.level", "OFF"));
        return properties;
    }


    private static Properties loadBdConfigurations(FileConfiguration configuration) throws NullPointerException {
        Properties properties = new Properties();

            properties.setProperty(Environment.JAKARTA_JDBC_DRIVER, getDatabaseDriver((Objects.requireNonNull(configuration.getString("db.dbms")))));
            properties.setProperty(Environment.JAKARTA_JDBC_URL, "jdbc:" +
                    configuration.getString("db.dbms") + "://" + configuration.getString("db.host") +
                    ":" + configuration.getString("db.port") + "/" + configuration.getString("db.database"));
            properties.setProperty(Environment.JAKARTA_JDBC_USER, configuration.getString("db.username"));
            properties.setProperty(Environment.JAKARTA_JDBC_PASSWORD, configuration.getString("db.password"));
            properties.setProperty(Environment.DIALECT, getDatabaseDialect((Objects.requireNonNull(configuration.getString("db.dbms")))));
        Settings.dbProperties = properties;
        return properties;
    }

    private static String getDatabaseDriver(String dbms){
        switch (dbms) {
            case "mysql":
                return "com.mysql.cj.jdbc.Driver";
            case "postgresql":
                return "org.postgresql.Driver";
            case "sqlserver":
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case "oracle":
                return "oracle.jdbc.driver.OracleDriver";
            case "sqlite":
                return "org.sqlite.JDBC";
        }
        return null;
    }

    private static String getDatabaseDialect(String dbms){
        switch (dbms) {
            case "mysql":
                return "org.hibernate.dialect.MySQLDialect";
            case "postgresql":
                return "org.hibernate.dialect.PostgreSQLDialect";
            case "sqlserver":
                return "org.hibernate.dialect.SQLServerDialect";
            case "oracle":
                return "org.hibernate.dialect.OracleDialect";
            case "sqlite":
                return "org.hibernate.dialect.SQLiteDialect";
        }
        return null;
    }


    private static String getPluginFolder() {
        return serverDirectory + fileSeparator + "plugins" + fileSeparator + pluginName;
    }

    /**
     * Get the path of the file inside the plugin folder
     *
     * @param fileName name of the file
     * @return path of the file
     */
    public static String getPluginFilePath(String fileName) {
        return getPluginFolder() + fileSeparator + fileName;
    }

    /**
     * Get the path of the messages file
     *
     * @return path of the messages file
     */
    public static String getMessagesFilePath() {
        return getPluginFilePath(messagesFileName);
    }

    /**
     * Map the messages from the messages file to default messages
     */
    private static void generateMessages() {
        // Load messages from the messages file
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(getMessagesFilePath())) {
            properties.load(input);

            // Check if the file has any message
            if (!properties.stringPropertyNames().isEmpty()) {
                // Map the messages to the default messages
                properties.stringPropertyNames().forEach(key -> messages.put(key, properties.getProperty(key)));
                anyMessageLoaded = true;
            }

        } catch (FileNotFoundException e) {
            RemoteAuth.logger.atWarn().log("[RemoteAuth] - MainThread --o-o-- Messages file not found");
        } catch (Exception e) {
            RemoteAuth.logger.atError().log("[RemoteAuth] - MainThread --o-o-- Error during messages file loading");
        }
    }

    /**
     * Set a new set of messages (mainly used to test the application)
     *
     * @param messages new set of messages
     */
    public static void setMessages(HashMap<String, String> messages) {
        Settings.messages = messages;
        Settings.anyMessageLoaded = true;
    }

    /**
     * Get the base message from the default messages and replace the color code character
     *
     * @param message default message to be retrieved
     * @return base message correctly formatted
     */
    private static String getBaseMessage(DefaultMessages message) {
        return (anyMessageLoaded && messages.containsKey(message.path)) ? messages.get(message.path).replace('&', '§') : message.message;
    }

    /**
     * Get the message from the default messages and replace the chat prefix
     * </p>
     * Done in this way, is possible to add a chat prefix to all the messages in an easy way
     *
     * @param message default message to be retrieved
     * @return message correctly formatted with the chat prefix
     */
    public static String getMessage(DefaultMessages message) {
        return !message.equals(DefaultMessages.CHAT_PREFIX) ?
                getBaseMessage(message).replace(Parameters.CHAT_PREFIX.parameter, getBaseMessage(DefaultMessages.CHAT_PREFIX))
                : getBaseMessage(message);
    }

    /**
     * Get the message from the default messages and replace the parameters
     *
     * @param message    default message to be retrieved
     * @param parameters hashmap with the parameters to be replaced
     * @return message correctly formatted with the parameters
     */
    public static String getMessage(DefaultMessages message, HashMap<Parameters, String> parameters) {
        String msg = getMessage(message);
        for (Parameters parameter : parameters.keySet()) {
            msg = msg.replace(parameter.parameter, parameters.get(parameter));
        }
        return msg;
    }

    public static Properties getDbProperties() {
        return dbProperties;
    }


}
