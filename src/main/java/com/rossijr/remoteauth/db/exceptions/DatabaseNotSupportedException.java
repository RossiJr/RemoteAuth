package com.rossijr.remoteauth.db.exceptions;

public class DatabaseNotSupportedException extends RuntimeException{
    public DatabaseNotSupportedException(String message){
        super(message);
    }
}
