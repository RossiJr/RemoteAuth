package com.rossijr.remoteauth.config.messages;

/**
 * Default parameters for messages
 */
public enum Parameters {
    CHAT_PREFIX("%chatPrefix%"),
    PLAYER("%player%"),
    X("%x%"),
    Y("%y%"),
    Z("%z%");

    public final String parameter;

    Parameters(String parameter) {
        this.parameter = parameter;
    }
}
