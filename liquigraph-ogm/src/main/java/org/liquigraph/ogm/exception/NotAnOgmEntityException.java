package org.liquigraph.ogm.exception;

public class NotAnOgmEntityException extends Exception{
    public NotAnOgmEntityException(String message) {
        super(message);
    }

    public NotAnOgmEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
