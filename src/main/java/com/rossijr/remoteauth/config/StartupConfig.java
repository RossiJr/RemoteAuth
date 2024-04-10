package com.rossijr.remoteauth.config;

public class StartupConfig {
    /**
     * Load the correct database driver.
     * </p>
     * This method should be called before any database operation.
     * </p>
     * Example:
     * <pre>
     *     StartupConfig.bdStartup("com.mysql.cj.jdbc.Driver");
     *     Connection connection = DriverManager.getConnection("{url}");
     *     ...
     *     connection.close();
     *     </pre>
     * @param driverClassName Database driver class name
     * @throws RuntimeException if the driver class is not found or cannot be loaded
     */
    public static void bdStartup(String driverClassName) throws RuntimeException{
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading database driver", e);
        }
    }
}
