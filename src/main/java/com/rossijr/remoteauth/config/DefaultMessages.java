package com.rossijr.remoteauth.config;

public class DefaultMessages {
    public static final String PREFIX = "§7[§bRemoteAuth§7] ";

    // Greeting messages
    public static final String JOIN_REGISTER_INFO = PREFIX + "§aYou are not registered!\n> §aUse /register <password> <confirmPassword> to register.";
    public static final String JOIN_LOGIN_INFO = PREFIX + "§aTo log in, use /login <password>";

    // Error messages
    public static final String NO_PERMISSION_TO_PERFORM_COMMAND = PREFIX + "§cYou don't have permission to perform this command.";
    public static final String PLAYER_ALREADY_LOGGED_IN = PREFIX + "§cYou are already logged in.";
    public static final String PLAYER_NOT_LOGGED_IN = PREFIX + "§cYou are not logged in.";
    public static final String INVALID_USERNAME_OR_PASSWORD = PREFIX + "§cInvalid username or password.";
    public static final String COMMAND_NOT_FOUND = PREFIX + "§cCommand not found.";
    public static final String SERVER_NOT_UP = PREFIX + "§cThe server is not up yet, try again later.";

    public static final String CRITICAL_LOG_IN_ERROR = PREFIX + "§c§lA critical error occurred while logging in. Please contact an administrator.";
    public static final String CRITICAL_REGISTER_ERROR = PREFIX + "§c§lA critical error occurred while registering. Please contact an administrator.";
    public static final String CRITICAL_ISREGISTERED_ERROR = PREFIX + "§c§lA critical error occurred while checking if registered. Please contact an administrator.";

    public static final String PASSWORDS_DO_NOT_MATCH = PREFIX + "§cPasswords do not match.";

    public static final String LOGIN_USAGE = PREFIX + "§cUsage: /login <password>";
    public static final String REGISTER_USAGE = PREFIX + "§cUsage: /register <password> <confirmPassword>";

    // Success messages
    public static final String LOGIN_SUCCESS = PREFIX + "§aYou have successfully logged in.";
    public static final String REGISTER_SUCCESS = PREFIX + "§aYou have successfully registered.";

    public static final String SPAWN_SET = PREFIX + "§aSpawn set at (§f${x}§a, §f${y}§a, §f${z}§a)";

}
