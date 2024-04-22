package com.rossijr.remoteauth.db.exceptions;


/**
 * Exception thrown when a column key is not found in the configuration file
 */
public class IllegalColumnKeyException extends RuntimeException{
    public IllegalColumnKeyException(String message) {
        super(message);
    }
}
