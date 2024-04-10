package com.rossijr.remoteauth;

import com.rossijr.remoteauth.authentication.Auth;
import com.rossijr.remoteauth.commands.AdminCommands;
import com.rossijr.remoteauth.commands.LoginCommand;
import com.rossijr.remoteauth.commands.RegisterCommand;
import com.rossijr.remoteauth.config.StartupConfig;
import com.rossijr.remoteauth.db.DbConnection;
import com.rossijr.remoteauth.models.SessionModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * <p>RemoteAuth plugin main class.</p>
 * <p>This class is responsible for the plugin initialization and event handling.</p>
 * <p>It also contains the active sessions map, spawn location, and other important variables.</p>
 */
public final class RemoteAuth extends JavaPlugin implements Listener {
    /**
     * Map of active sessions, where the key is the player's UUID and the value is the session model
     */
    private Map<UUID, SessionModel> activeSessions;
    /**
     * If the plugin is up, used to prevent players from joining before the plugin is ready
     */
    private boolean pluginUp = false;

    // -----

    /**
     * <p>Spawn location coordinates.</p>
     * <ul>
     *     <li>[0] = x</li>
     *     <li>[1] = y</li>
     *     <li>[2] = z</li>
     * </ul>
     */
    private int[] spawnCoord;
    /**
     * <p>Spawn location angles.</p>
     * <ul>
     *     <li>[0] = pitch</li>
     *     <li>[1] = yaw</li>
     * </ul>
     */
    private float[] spawnAngles;
    /**
     * Spawn world name.
     */
    private String spawnWorld;

    // -----

    /**
     * If the spawn is setted
     */
    private boolean spawnSetted = false;


    public Map<UUID, SessionModel> getActiveSessions() {
        return activeSessions;
    }


    @Override
    public void onEnable() {
        // Register the commands
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("users").setExecutor(new AdminCommands(this));
        getCommand("rasetspawn").setExecutor(new AdminCommands(this));
        getCommand("permtest").setExecutor(new AdminCommands(this));

        // Initialize the active sessions map, guaranteeing that it is not null and empty
        activeSessions = new HashMap<>();

        // Load the configuration
        getConfig().options().copyDefaults();
        try {
            // Check if the spawn is setted
            spawnSetted = getConfig().getString("spawn.world") != null;
        } catch (Exception e) {
            spawnSetted = false;
        }

        // If the spawn is setted, load spawn location
        if (spawnSetted)
            setLocalSpawn(new Location(Bukkit.getWorld(getConfig().getString("spawn.world")), getConfig().getInt("spawn.x"),
                    getConfig().getInt("spawn.y"), getConfig().getInt("spawn.z"),
                    Float.valueOf(getConfig().getString("spawn.yaw")), Float.valueOf(getConfig().getInt("spawn.pitch"))));

        // Load the database driver
        StartupConfig.bdStartup("org.postgresql.Driver");

        // Use this class to register events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Set the plugin as up, which means the plugin has already started
        pluginUp = true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * <p>Set the spawn location in the running server.</p>
     *
     * @param location The location to set as spawn
     */
    private void setLocalSpawn(Location location) {
        this.spawnCoord = new int[3];
        this.spawnAngles = new float[2];

        this.spawnCoord[0] = location.getBlockX();
        this.spawnCoord[1] = location.getBlockY();
        this.spawnCoord[2] = location.getBlockZ();
        this.spawnAngles[1] = location.getYaw();
        this.spawnAngles[0] = location.getPitch();
        this.spawnWorld = location.getWorld().getName();
    }

    /**
     * <p>Set the spawn location in the configuration file.</p>
     *
     * @param location The location to set as spawn
     */
    private void setConfigSpawn(Location location) {
        getConfig().options().copyDefaults();
        getConfig().set("spawn.x", location.getBlockX());
        getConfig().set("spawn.y", location.getBlockY());
        getConfig().set("spawn.z", location.getBlockZ());
        getConfig().set("spawn.yaw", location.getYaw());
        getConfig().set("spawn.pitch", location.getPitch());
        getConfig().set("spawn.world", location.getWorld().getName());
        saveConfig();
    }

    /**
     * <p>Set the spawn location in the configuration file and in the running server.</p>
     *
     * @param location The location to set as spawn
     */
    public void setSpawn(Location location) {
        setConfigSpawn(location);
        setLocalSpawn(location);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // If the spawn is setted, teleport the player to the spawn location
        if (spawnSetted)
            e.getPlayer().teleport(new Location(Bukkit.getWorld(spawnWorld), spawnCoord[0], spawnCoord[1], spawnCoord[2],
                    spawnAngles[1], spawnAngles[0]));
        try {
            // Check if the player is registered
            if (Auth.isUnregistered(e.getPlayer().getUniqueId(), e.getPlayer().getName(), DbConnection.connect())) {
                e.getPlayer().sendMessage(ChatColor.RED + "You are not registered! Use /register <password> <confirmPassword>");
            } else {
                e.getPlayer().sendMessage(ChatColor.GREEN + "To log in, use /login <password>");
            }
        } catch (Exception ex) {
            e.getPlayer().sendMessage(ChatColor.RED + "An error happened while checking if you are registered, contact an administrator");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        // If the player is not logged in, cancel the event
        if (!activeSessions.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        // If the player is not logged in, cancel the event
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)
                || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                || e.getAction().equals(Action.LEFT_CLICK_AIR)
                || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            if (!activeSessions.containsKey(player.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        // If the player is not logged in, cancel the event
        if (!activeSessions.containsKey(event.getPlayer().getUniqueId())) {
            String[] message = event.getMessage().split(" ");
            if (!(message[0].equals("/login") || message[0].equals("/register"))) {
                event.getPlayer().sendMessage(ChatColor.RED + "You are not logged in!");
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        // If the plugin is not up, cancel the player from joining
        if (!pluginUp) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "The server is not up yet, try again later");
        }

        // If the player is already logged in, cancel the player from joining
        if (activeSessions.containsKey(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You are already logged in!");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove the player from the active sessions map
        activeSessions.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        // If the player is not logged in, cancel the event
        if (!activeSessions.containsKey(event.getPlayer().getUniqueId())) {
            String[] message = event.getMessage().split(" ");
            if (!(message[0].equals("/login") || message[0].equals("/register"))) {
                event.getPlayer().sendMessage(ChatColor.RED + "You are not logged in!");
                event.setCancelled(true);
            }
        }
    }
}
