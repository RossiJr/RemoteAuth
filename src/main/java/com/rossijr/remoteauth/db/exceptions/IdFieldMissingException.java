package com.rossijr.remoteauth.db.exceptions;

public class IdFieldMissingException extends RuntimeException {
    public IdFieldMissingException(String message) {
        super(message);
    }
}
