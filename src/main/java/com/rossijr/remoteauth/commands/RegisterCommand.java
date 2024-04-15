package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.authentication.Auth;
import com.rossijr.remoteauth.authentication.dao.RegisterDAO;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.db.DbConnection;
import com.rossijr.remoteauth.models.SessionModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.sql.Connection;

public class RegisterCommand implements CommandExecutor {

    /**
     * Plugin instance
     */
    private RemoteAuth plugin;
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
                        try (Connection conn = DbConnection.connect()) {
                            // Checks if the username and UUID are already registered and other important checks
                            String allowedToRegister = Auth.allowedToRegister(player.getName(), conn);
                            if (allowedToRegister == null) {
                                // Registers the user, if successful, adds the user to the active sessions
                                if (Auth.register(player.getUniqueId(), player.getName(), password, conn)) {
                                    plugin.getActiveSessions().put(player.getUniqueId(), new SessionModel(new UserModel(player.getUniqueId(), player.getName())));
                                    player.sendMessage(Settings.getMessage(DefaultMessages.REGISTER_SUCCESS));
                                } else {
                                    player.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_REGISTER_ERROR));
                                }
                            } else {
                                // If the user is not allowed to register, sends the message explaining why to the player
                                player.sendMessage(ChatColor.RED + allowedToRegister);
                            }
                        } catch (Exception e) {
                            player.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_REGISTER_ERROR));
                            System.out.println("RemoteAuth --/ERROR/-- Error performing user registration - class {" + RegisterDAO.class.getName() + "}");
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
