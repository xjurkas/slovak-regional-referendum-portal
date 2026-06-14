package com.oop.referendumserver.services.exceptions;

/**
 * Exception class representing an error related to user operations.
 */
public class UserException extends RuntimeException {
    /**
     * Constructs a new UserException with the specified detail message
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public UserException(String message) {
        super(message);
    }
}
