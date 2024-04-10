package com.rossijr.remoteauth.models;
import com.rossijr.remoteauth.authentication.models.UserModel;

import java.util.Calendar;

/**
 * SessionModel class
 * <p>
 * Designed to store session data
 */
public class SessionModel {
    private UserModel user;
    private long expirationTime = -1L;
    private Calendar creationTime;
    private Long ipv4 = null;

    public SessionModel(UserModel user) {
        this.user = user;
        this.creationTime = Calendar.getInstance();
    }


    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Long getIpv4() {
        return ipv4;
    }

    public void setIpv4(Long ipv4) {
        this.ipv4 = ipv4;
    }
}
