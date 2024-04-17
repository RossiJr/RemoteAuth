package com.rossijr.remoteauth.config.messages;

import com.rossijr.remoteauth.config.Settings;

/**
 * Default messages for each message interaction with the player
 * <p>
 *     The usage of each message is defined in the project's README.md
 */
public enum DefaultMessages {
    CHAT_PREFIX("prefix.chat_prefix", "§7[§bRemoteAuth§7] "),

    WELCOME_REGISTER("messages.welcome.register", Settings.getMessage(CHAT_PREFIX) + "§aYou are not registered!\n> §aUse /register <password> <confirmPassword> to register."),
    WELCOME_LOGIN("messages.welcome.login", Settings.getMessage(CHAT_PREFIX) + "§aTo log in, use /login <password>"),

    LOGIN_SUCCESS("messages.welcome.login_success", Settings.getMessage(CHAT_PREFIX) + "§aYou have successfully logged in!"),
    REGISTER_SUCCESS("messages.welcome.register_success", Settings.getMessage(CHAT_PREFIX) + "§aYou have been successfully registered!"),

    ERROR_NO_PERMISSION("messages.error.no_permission", Settings.getMessage(CHAT_PREFIX) + "§cYou don't have permission to do that."),
    ERROR_ALREADY_LOGGED_IN("messages.error.already_logged_in", Settings.getMessage(CHAT_PREFIX) + "§cYou are already logged in."),
    ERROR_NOT_LOGGED_IN("messages.error.not_logged_in", Settings.getMessage(CHAT_PREFIX) + "§cYou are not logged in."),
    ERROR_INVALID_USERNAME_PASSWORD("messages.error.invalid_username_password", Settings.getMessage(CHAT_PREFIX) + "§cInvalid username or password."),
    ERROR_COMMAND_NOT_FOUND("messages.error.command_not_found", Settings.getMessage(CHAT_PREFIX) + "§cCommand not found."),
    ERROR_SERVER_NOT_UP("messages.error.server_not_up", Settings.getMessage(CHAT_PREFIX) + "§cThe server is not up yet, try again later."),

    CRITICAL_LOG_IN_ERROR("messages.error.critical_login_error", Settings.getMessage(CHAT_PREFIX) + "§c§lA critical error occurred while logging in. Please contact an administrator."),
    CRITICAL_REGISTER_ERROR("messages.error.critical_register_error", Settings.getMessage(CHAT_PREFIX) + "§c§lA critical error occurred while registering. Please contact an administrator."),
    CRITICAL_ISREGISTERED_ERROR("messages.error.critical_isregistered_error", Settings.getMessage(CHAT_PREFIX) + "§c§lA critical error occurred while checking if you are registered. Please contact an administrator."),

    ERROR_PASSWORDS_NOT_MATCH("messages.error.passwords_do_not_match", Settings.getMessage(CHAT_PREFIX) + "§cPasswords do not match."),

    ERROR_LOGIN_USAGE("messages.error.login_usage", Settings.getMessage(CHAT_PREFIX) + "§cUsage: /login <password>"),
    ERROR_REGISTER_USAGE("messages.error.register_usage", Settings.getMessage(CHAT_PREFIX) + "§cUsage: /register <password> <confirmPassword>"),

    SUCCESS_SPAWN_SET("messages.success.spawn_set", Settings.getMessage(CHAT_PREFIX) + "§aSpawn set at (§f%x%§a, §f%y%§a, §f%z%§a)"),

    SUCCESS_PASSWORD_CHANGED("messages.success.password_changed", Settings.getMessage(CHAT_PREFIX) + "§aPassword changed successfully."),
    ERROR_INVALID_ARGUMENTS_PASSWORD_CHANGE("messages.error.invalid_arguments_password_change", Settings.getMessage(CHAT_PREFIX) + "§cInvalid arguments. Usage: /changepassword <password>"),
    ERROR_PASSWORD_CHANGE("messages.error.password_change", Settings.getMessage(CHAT_PREFIX) + "§cError while changing password."),
    CRITICAL_PASSWORD_CHANGE_ERROR("messages.error.critical_password_change_error", Settings.getMessage(CHAT_PREFIX) + "§c§lA critical error occurred while changing the password. Please contact an administrator."),


    ERROR_PLAYER_NOT_FOUND("messages.error.player_not_found", Settings.getMessage(CHAT_PREFIX) + "§cPlayer not found.");

    public final String path;
    public final String message;

    DefaultMessages(String path, String message) {
        this.path = path;
        this.message = message;
    }

}
