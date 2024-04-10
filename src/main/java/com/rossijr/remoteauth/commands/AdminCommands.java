package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import org.bukkit.ChatColor;
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
    private RemoteAuth plugin;
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
                        player.sendMessage(ChatColor.GREEN + "Spawn set at ("
                                + ChatColor.WHITE + player.getLocation().getBlockX() + ChatColor.GREEN + ", "
                                + ChatColor.WHITE + player.getLocation().getBlockY() + ChatColor.GREEN + ", "
                                + ChatColor.WHITE + player.getLocation().getBlockZ() + ChatColor.GREEN + ")");
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "You don't have permission to use this command");
                    }
                }
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Command not found!");
                break;
        }
        return false;
    }
}
