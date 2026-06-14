package com.oop.referendumserver.view;

/**
 * Exception thrown when an unexpected error occurs during the loading of an FXML scene
 */
public class FxmlLoadException extends RuntimeException {
    /**
     * Returns the error message associated with this exception
     * @return The error message
     */
    @Override
    public String getMessage() {
        return "Unexpected error occurred during loading of a scene.";
    }
}
