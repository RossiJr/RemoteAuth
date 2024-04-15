package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.config.messages.ParameterBuilder;
import com.rossijr.remoteauth.config.messages.Parameters;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Admin commands
 * <p>
 *     This class is responsible for handling admin commands
 */
public class AdminCommands implements CommandExecutor {
    /**
     * Plugin instance
     */
    private final RemoteAuth plugin;
    public AdminCommands(RemoteAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Check which command was issued
        switch (command.getName()) {
            case "rasetspawn":
                if(commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    // Check if the player has permission to use the command
                    if(commandSender.hasPermission("remoteauth.setspawn")) {
                        plugin.setSpawn(player.getLocation());
                        player.sendMessage(Settings.getMessage(DefaultMessages.SUCCESS_SPAWN_SET, ParameterBuilder.create()
                                .addParameter(Parameters.X, String.valueOf(player.getLocation().getBlockX()))
                                .addParameter(Parameters.Y, String.valueOf(player.getLocation().getBlockY()))
                                .addParameter(Parameters.Z, String.valueOf(player.getLocation().getBlockZ()))
                                .build()));
                    } else {
                        commandSender.sendMessage(Settings.getMessage(DefaultMessages.ERROR_NO_PERMISSION));
                    }
                    return true;
                }
                break;
            default:
                commandSender.sendMessage(Settings.getMessage(DefaultMessages.ERROR_COMMAND_NOT_FOUND));
                break;
        }
        return false;
    }
}
