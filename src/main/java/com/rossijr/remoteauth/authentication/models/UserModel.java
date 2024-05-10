package com.rossijr.remoteauth.authentication.models;


import jakarta.persistence.*;

import java.util.UUID;

/**
 * UserModel class to represent the user in the database and in the application
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "uuid"),})
public class UserModel {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    public UserModel(){
    }


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

    @Override
    public String toString() {
        return "UserModel{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
