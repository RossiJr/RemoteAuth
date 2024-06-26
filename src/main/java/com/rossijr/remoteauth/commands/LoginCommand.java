package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.authentication.AuthManager;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.models.SessionModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class LoginCommand implements CommandExecutor {
    private final RemoteAuth plugin;

    public LoginCommand(RemoteAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Checks if the command has the correct usage
            if (args.length == 1) {
                // Checks if the player is logged in
                if (!plugin.getActiveSessions().containsKey(player.getUniqueId())) {
                    UUID uuid = ((Player) sender).getUniqueId();
                    String password = args[0];
                    try{
                        // Checks if the username and password are correct
                        UserModel user = AuthManager.login(uuid, password);
                        // If the user is correct, an UserModel object will be created and
                        // the user will be added to the active sessions
                        if(user != null){
                            plugin.doPostLogin(player.getUniqueId(), new SessionModel(user));
                            player.sendMessage(Settings.getMessage(DefaultMessages.LOGIN_SUCCESS));
                        } else {
                            player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_INVALID_USERNAME_PASSWORD));
                        }

                    } catch (Exception e) {
                        player.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_LOG_IN_ERROR));
                        System.out.println("RemoteAuth --/ERROR/-- Error performing user login - class {" + AuthManager.class.getName() + "}");
                    }
                } else {
                    player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_ALREADY_LOGGED_IN));
                }

            } else {
                player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_LOGIN_USAGE));
            }
        }

        return true;
    }
}