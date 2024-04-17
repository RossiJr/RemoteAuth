package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.authentication.Auth;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.db.DbConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                    try (var conn = DbConnection.connect()) {
                        if (Auth.changePassword(player.getUniqueId(), strings[0], conn)) {
                            player.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_PASSWORD_CHANGED));
                        } else {
                            player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORD_CHANGE));
                        }
                    } catch (Exception e) {
                        player.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_PASSWORD_CHANGE_ERROR));
                    }
                } else if (player.hasPermission("remoteauth.changePlayerPassword")) {
                    // Change password of another player
                    Player alteredPasswordPlayer = player.getServer().getPlayer(strings[0]);
                    if(alteredPasswordPlayer != null) {
                        try (var conn = DbConnection.connect()) {
                            if (Auth.changePassword(alteredPasswordPlayer.getUniqueId(), strings[1], conn)) {
                                player.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_PASSWORD_CHANGED));
                            } else {
                                player.sendMessage(Settings.getMessage(DefaultMessages.ERROR_PASSWORD_CHANGE));
                            }
                        } catch (Exception e) {
                            player.sendMessage(Settings.getMessage(DefaultMessages.CRITICAL_PASSWORD_CHANGE_ERROR));
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

        }
        return true;
    }
}
