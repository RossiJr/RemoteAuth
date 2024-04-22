package com.rossijr.remoteauth.db.exceptions;

/**
 * Exception thrown when an object cannot be built
 */
public class ObjectBuildingException extends RuntimeException{
    public ObjectBuildingException(String message, Throwable cause) {
        super(message, cause);
    }
}
