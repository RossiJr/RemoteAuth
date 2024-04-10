package com.rossijr.remoteauth.commands;

import com.rossijr.remoteauth.RemoteAuth;
import com.rossijr.remoteauth.authentication.Auth;
import com.rossijr.remoteauth.authentication.models.UserModel;
import com.rossijr.remoteauth.db.DbConnection;
import com.rossijr.remoteauth.models.SessionModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;


public class LoginCommand implements CommandExecutor {
    private RemoteAuth plugin;

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
                    String username = sender.getName();
                    String password = args[0];
                    try (Connection conn = DbConnection.connect()) {
                        // Checks if the username and password are correct
                        UserModel user = Auth.login(username, password, conn);
                        // If the user is correct, an UserModel object will be created and
                        // the user will be added to the active sessions
                        if(user != null){
                            plugin.getActiveSessions().put(player.getUniqueId(), new SessionModel(user));
                            player.sendMessage(ChatColor.GREEN + "You have successfully logged in");
                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid username or password");
                        }

                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "An error happened while logging in, contact an administrator");
                        System.out.println("RemoteAuth --/ERROR/-- Error performing user login - class {" + Auth.class.getName() + "}");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You are already logged in");
                }

            } else {
                player.sendMessage(ChatColor.RED + "Usage: /login <username> <password>");
            }
        }

            return true;
        }
    }
