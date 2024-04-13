package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.config.DefaultMessages;
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
                        player.sendMessage(DefaultMessages.SPAWN_SET
                                .replace("${x}", String.valueOf(player.getLocation().getBlockX()))
                                .replace("${y}", String.valueOf(player.getLocation().getBlockY()))
                                .replace("${z}", String.valueOf(player.getLocation().getBlockZ())));
                    } else {
                        commandSender.sendMessage(DefaultMessages.NO_PERMISSION_TO_PERFORM_COMMAND);
                    }
                }
                break;
            default:
                commandSender.sendMessage(DefaultMessages.COMMAND_NOT_FOUND);
                break;
        }
        return false;
    }
}
