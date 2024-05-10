package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.authentication.AuthManager;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Class used to change the password of a player
 */
public class ChangePasswordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            // Check if the player has the correct amount of arguments
            if (strings.length > 0 && strings.length < 3) {
                // Change the password of the player who executed the command
                if (strings.length == 1) {
                    if (AuthManager.changePassword(player.getUniqueId(), strings[0])) {
                        player.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_PASSWORD_CHANGED));
                    } else {
                        player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORD_CHANGE));
                    }
                } else if (player.hasPermission("remoteauth.changePlayerPassword")) {
                    // Change password of another player
                    Player alteredPasswordPlayer = player.getServer().getPlayer(strings[0]);
                    if (alteredPasswordPlayer != null) {
                        if (AuthManager.changePassword(alteredPasswordPlayer.getUniqueId(), strings[1])) {
                            player.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_PASSWORD_CHANGED));
                        } else {
                            player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORD_CHANGE));
                        }
                    } else {
                        player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PLAYER_NOT_FOUND));
                    }
                } else {
                    player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_NO_PERMISSION));
                }

            } else {
                player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_INVALID_ARGUMENTS_PASSWORD_CHANGE));
            }
        } else if (commandSender instanceof ConsoleCommandSender) { // Check if the command was executed by the console
            try {
                if (strings.length == 2) {
                    Player alteredPasswordPlayer = commandSender.getServer().getPlayer(strings[0]);
                    // Checks if the player is online, if it is the alteredPasswordPlayer will not be null
                    if (alteredPasswordPlayer == null) {
                        UserModel user = AuthManager.getUserByUsername(strings[0]);
                        // Checks if the player is registered in the database
                        if (user != null) {
                            user.setPassword(strings[1]);
                            if (AuthManager.changePassword(user.getUuid(), strings[1])) {
                                commandSender.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_PASSWORD_CHANGED));
                            } else {
                                commandSender.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORD_CHANGE));
                            }
                        } else {
                            commandSender.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PLAYER_NOT_FOUND));
                        }
                        return true;
                    }
                    if (AuthManager.changePassword(alteredPasswordPlayer.getUniqueId(), strings[1])) {
                        alteredPasswordPlayer.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_PASSWORD_CHANGED));
                    } else {
                        alteredPasswordPlayer.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORD_CHANGE));
                    }
                } else {
                    commandSender.sendMessage(Settings.getMessage(DefaultMessages.ERROR_INVALID_ARGUMENTS_PASSWORD_CHANGE));
                }
            } catch (Exception e) {
                commandSender.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_PASSWORD_CHANGE_ERROR));
            }
        }
        return true;
    }
}
