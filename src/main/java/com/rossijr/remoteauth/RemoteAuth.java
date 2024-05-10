package com.rossijr.remoteauth;

import com.rossijr.remoteauth.authentication.AuthManager;
import com.rossijr.remoteauth.authentication.models.Task;
import com.rossijr.remoteauth.commands.AdminCommands;
import com.rossijr.remoteauth.commands.ChangePasswordCommand;
import com.rossijr.remoteauth.commands.LoginCommand;
import com.rossijr.remoteauth.commands.RegisterCommand;
import com.rossijr.remoteauth.config.Settings;
import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.config.messages.ParameterBuilder;
import com.rossijr.remoteauth.config.messages.Parameters;
import com.rossijr.remoteauth.models.SessionModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * <p>RemoteAuth plugin main class.</p>
 * <p>This class is responsible for the plugin initialization and event handling.</p>
 * <p>It also contains the active sessions map, spawn location, and other important variables.</p>
 */
public final class RemoteAuth extends JavaPlugin implements Listener {
    private static final Logger logger = LogManager.getLogger(RemoteAuth.class);
    /**
     * Map of active sessions, where the key is the player's UUID and the value is the session model
     */
    private Map<UUID, SessionModel> activeSessions;
    private Map<UUID, List<Task>> taskMap;

    private final Map<String, Boolean> autoMessages = new HashMap<>();

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
    private int[] spawnCoordinates;
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
     * If the spawn is set
     */
    private boolean spawnSet = false;


    public Map<UUID, SessionModel> getActiveSessions() {
        return activeSessions;
    }


    @Override
    public void onEnable() {
        // Get the config file
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        if(getConfig().getBoolean("db.ready_to_connect")) {
            // Initialize the messages and database
            try {
                Settings.init(getConfig());
            } catch (Exception e) {
                System.out.println("RemoteAuth --/ERROR/-- Error initializing messages and database");
                Bukkit.shutdown();
            }
        } else {
            System.out.println("RemoteAuth --/ERROR/-- Plugin is disabled in the configuration file");
        }

        /// Checks the spawn to see if it is set
        try {
            // Check if the spawn is set
            spawnSet = getConfig().getBoolean("spawn.spawn_set");
            logger.atInfo().log("Spawn set: {}", spawnSet);
        } catch (Exception e) {
            spawnSet = false;
        }
        /// If the spawn is set, load the spawn location
        if (spawnSet) {
            try {
                setLocalSpawn(new Location(Bukkit.getWorld(Objects.requireNonNull(getConfig().getString("spawn.world"))), getConfig().getInt("spawn.X"),
                        getConfig().getInt("spawn.Y"), getConfig().getInt("spawn.Z"),
                        Float.parseFloat(Objects.requireNonNull(getConfig().getString("spawn.yaw"))), (float) getConfig().getInt("spawn.pitch")));
            } catch (NullPointerException e) {
                System.out.println("Error loading spawn location from configuration file");
                spawnSet = false;
            }
        }

        /// Loads the auto messages configuration
        autoMessages.put("automatic_messages.alert.force_logout_30s", getConfig().getBoolean("automatic_messages.alert.force_logout_30s"));
        autoMessages.put("automatic_messages.alert.force_logout_15s", getConfig().getBoolean("automatic_messages.alert.force_logout_15s"));
        autoMessages.put("automatic_messages.alert.force_logout_5s", getConfig().getBoolean("automatic_messages.alert.force_logout_5s"));


        // Register the commands
        try {
            Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCommand(this));
            Objects.requireNonNull(getCommand("register")).setExecutor(new RegisterCommand(this));
            Objects.requireNonNull(getCommand("changepassword")).setExecutor(new ChangePasswordCommand());
            Objects.requireNonNull(getCommand("rasetspawn")).setExecutor(new AdminCommands(this));
        } catch (NullPointerException e) {
            Bukkit.shutdown();
            throw new RuntimeException("Error initializing commands");
        }
        // Initialize the active sessions map, guaranteeing that it is not null and empty
        activeSessions = new HashMap<>();

        // Initialize the task map, guaranteeing that it is not null and empty
        taskMap = new HashMap<>();

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
    private void setLocalSpawn(Location location) throws NullPointerException {
        this.spawnCoordinates = new int[3];
        this.spawnAngles = new float[2];

        this.spawnCoordinates[0] = location.getBlockX();
        this.spawnCoordinates[1] = location.getBlockY();
        this.spawnCoordinates[2] = location.getBlockZ();
        this.spawnAngles[1] = location.getYaw();
        this.spawnAngles[0] = location.getPitch();
        this.spawnWorld = Objects.requireNonNull(location.getWorld()).getName();
    }

    /**
     * <p>Set the spawn location in the configuration file.</p>
     *
     * @param location The location to set as spawn
     */
    private void setConfigSpawn(Location location) throws NullPointerException {
        try {
            getConfig().options().copyDefaults();
            getConfig().set("spawn.spawn_set", true);
            getConfig().set("spawn.X", location.getBlockX());
            getConfig().set("spawn.Y", location.getBlockY());
            getConfig().set("spawn.Z", location.getBlockZ());
            getConfig().set("spawn.yaw", location.getYaw());
            getConfig().set("spawn.pitch", location.getPitch());
            getConfig().set("spawn.world", Objects.requireNonNull(location.getWorld()).getName());
        } catch (NullPointerException e) {
            System.out.println("Error setting spawn location in configuration file");
        } finally {
            saveConfig();
        }
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
        if(autoMessages.get("automatic_messages.alert.force_logout_5s")) {
            BukkitTask taskPreKicking2 = Bukkit.getScheduler().runTaskLater(this, () -> e.getPlayer().sendMessage(Settings.getMessage(DefaultMessages.PRE_LOGIN_TIME_OUT_ALERT, ParameterBuilder.create()
                    .addParameter(Parameters.TIME, "5")
                    .build())), 500);
            addTask(e.getPlayer().getUniqueId(), new Task(taskPreKicking2, true));
        }

        if(autoMessages.get("automatic_messages.alert.force_logout_15s")) {
            BukkitTask taskPreKicking = Bukkit.getScheduler().runTaskLater(this, () -> e.getPlayer().sendMessage(Settings.getMessage(DefaultMessages.PRE_LOGIN_TIME_OUT_ALERT, ParameterBuilder.create()
                    .addParameter(Parameters.TIME, "15")
                    .build())), 300);
            addTask(e.getPlayer().getUniqueId(), new Task(taskPreKicking, true));
        }

        if(autoMessages.get("automatic_messages.alert.force_logout_30s")) {
            BukkitTask taskKickIfNotLoggedIn = Bukkit.getScheduler().runTaskLater(this, () -> {
                if (!isPlayerLogged(e.getPlayer().getUniqueId())) {
                    e.getPlayer().kickPlayer(Settings.getMessage(DefaultMessages.LOGIN_TIME_OUT_ALERT));
                }
            }, 600);
            addTask(e.getPlayer().getUniqueId(), new Task(taskKickIfNotLoggedIn, true));
        }
        // If the spawn is set, teleport the player to the spawn location
        if (spawnSet)
            e.getPlayer().teleport(new Location(Bukkit.getWorld(spawnWorld), spawnCoordinates[0], spawnCoordinates[1], spawnCoordinates[2],
                    spawnAngles[1], spawnAngles[0]));
        try {
            // Check if the player is registered
            if (AuthManager.isAllowedToRegister(e.getPlayer().getUniqueId())) {
                e.getPlayer().sendMessage(Settings.getMessage(DefaultMessages.WELCOME_REGISTER, ParameterBuilder.create()
                        .addParameter(Parameters.PLAYER, e.getPlayer().getName())
                        .build()));
            } else {
                e.getPlayer().sendMessage(Settings.getMessage(DefaultMessages.WELCOME_LOGIN, ParameterBuilder.create()
                        .addParameter(Parameters.PLAYER, e.getPlayer().getName())
                        .build()));
            }
        } catch (Exception ex) {
            Objects.requireNonNull(Bukkit.getPlayer(e.getPlayer().getUniqueId())).kickPlayer(Settings.getMessage(DefaultMessages.CRITICAL_ISREGISTERED_ERROR));
            System.out.println("RemoteAuth --/ERROR/-- Error checking if " + e.getPlayer().getName() + " was registered - class {" + AuthManager.class.getName() + "}");
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        // If the player is not logged in, cancel the event
        if (!isPlayerLogged(event.getPlayer().getUniqueId())) {
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
            if (!isPlayerLogged(player.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        // If the player is not logged in, cancel the event
        if (!isPlayerLogged(event.getPlayer().getUniqueId())) {
            String[] message = event.getMessage().split(" ");
            if (!(message[0].equals("/login") || message[0].equals("/register"))) {
                event.getPlayer().sendMessage(Settings.getMessage(DefaultMessages.ERROR_NOT_LOGGED_IN));
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        // If the plugin is not up, cancel the player from joining
        if (!pluginUp) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Settings.getMessage(DefaultMessages.ERROR_SERVER_NOT_UP));
        }

        // If the player is already logged in, cancel the player from joining
        if (isPlayerLogged(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Settings.getMessage(DefaultMessages.ERROR_ALREADY_LOGGED_IN));
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
        if (!isPlayerLogged(event.getPlayer().getUniqueId())) {
            String[] message = event.getMessage().split(" ");
            if (!(message[0].equals("/login") || message[0].equals("/register"))) {
                event.getPlayer().sendMessage(Settings.getMessage(DefaultMessages.ERROR_NOT_LOGGED_IN));
                event.setCancelled(true);
            }
        }
    }

    /**
     * <p>Method used to check if the player is logged in.</p>
     * @param playerUUID The player's UUID
     * @return If the player is logged in
     */
    private boolean isPlayerLogged(UUID playerUUID) {
        return activeSessions.containsKey(playerUUID);
    }

    /**
     * <p>Method used to add a task to the player's login tasks.</p>
     * @param playerUUID The player's UUID
     * @param task The new task to add
     */
    public void addTask(UUID playerUUID, Task task) {
        taskMap.computeIfAbsent(playerUUID, k -> new ArrayList<>());
        taskMap.get(playerUUID).add(task);
    }

    /**
     * <p>Method used to clear all the player's login tasks.</p>
     * @param playerUUID The player's UUID
     */
    public void clearLoginTasks(UUID playerUUID) {
        // Checks first if the player is in the task map
        if (taskMap.get(playerUUID) != null) {
            // If so, cancel all the player's login tasks
            taskMap.get(playerUUID).stream().filter(Task::isPreLogin).forEach(Task::cancelTask);
        }
    }

    /**
     * <p>Method used to do all the player's post login procedures.</p>
     * <p>Currently it does the following:</p>
     * <ul>
     *     <li>Clears the player's login tasks</li>
     *     <li>Adds the player to the active sessions map</li>
     * </ul>
     * @param playerUUID The player's UUID
     * @param sessionModel The session model
     */
    public void doPostLogin(UUID playerUUID, SessionModel sessionModel) {
        clearLoginTasks(playerUUID);
        this.activeSessions.put(playerUUID, sessionModel);
    }
}