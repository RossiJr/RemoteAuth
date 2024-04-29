package com.rossijr.remoteauth.authentication.models;

import org.bukkit.scheduler.BukkitTask;

/**
 * Task model used to store the task and its type
 */
public class Task {
    /**
     * BukkitTask instance
     */
    private final BukkitTask task;

    /**
     * The type of the task
     */
    private boolean isPreLogin;

    public Task(BukkitTask task, boolean isPreLogin) {
        this.task = task;
        this.isPreLogin = isPreLogin;
    }

    public boolean isPreLogin() {
        return isPreLogin;
    }

    public void cancelTask() {
        task.cancel();
    }
}
