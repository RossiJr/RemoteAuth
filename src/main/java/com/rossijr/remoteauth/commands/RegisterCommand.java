package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.authentication.AuthManager;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.db.DbConnection;
import com.rossijr.remoteauth.models.SessionModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class RegisterCommand implements CommandExecutor {

    /**
     * Plugin instance
     */
    private final RemoteAuth plugin;

    public RegisterCommand(RemoteAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Checks if the player is logged in
            if (!plugin.getActiveSessions().containsKey(player.getUniqueId())) {
                // Checks if the command has the correct usage
                if (args.length == 2) {
                    String password = args[0], confirmPassword = args[1];
                    // Checks if the password and confirm password are the same
                    if (password.equals(confirmPassword)) {
                        // Checks if the username and UUID are already registered and other important checks
                        if (AuthManager.isAllowedToRegister(player.getUniqueId())) {
                            // Registers the user, if successful, adds the user to the active sessions
                            if (AuthManager.register(new UserModel(player.getUniqueId(), player.getName(), password))) {
                                plugin.doPostLogin(player.getUniqueId(), new SessionModel(new UserModel(player.getUniqueId(), player.getName())));
                                player.sendMessage(Settings.getMessage(DefaultMessages.REGISTER_SUCCESS));
                            } else {
                                player.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_REGISTER_ERROR));
                            }
                        } else {
                            player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_ALREADY_REGISTERED));
                        }
                    } else {
                        player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORDS_NOT_MATCH));
                    }
                } else {
                    sender.sendMessage(Settings.getMessage(DefaultMessages.ERROR_REGISTER_USAGE));
                }
            } else {
                player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_ALREADY_LOGGED_IN));
            }
        }
        return true;
    }
}