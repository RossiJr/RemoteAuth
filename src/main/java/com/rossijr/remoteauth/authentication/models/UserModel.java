package com.rossijr.remoteauth.authentication.models;

import java.util.UUID;

/**
 * UserModel class to represent the user in the database and in the application
 */
public class UserModel {
    private UUID uuid;
    private String username;
    private String password;


    public UserModel(UUID uuid, String username, String password) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
    }

    public UserModel(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
