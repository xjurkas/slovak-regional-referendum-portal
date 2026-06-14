package com.oop.referendumserver.view.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.view.FxmlLoadException;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Abstract base class for JavaFX view controllers.
 * Provides common methods for loading scenes and displaying error messages.
 */
public abstract class AbstractViewController {

    /**
     * Loads a new scene using the specified FXML file.
     * If an exception occurs during the loading process, an error message is displayed
     * @param sceneFxml the FXML file path of the scene to load
     */
    protected void loadScene(String sceneFxml) {
        try {
            ReferendumJavaFxApplication.setRoot(sceneFxml);
        } catch (FxmlLoadException ex) {
            showSnackBarError(ex.getMessage());
        }
    }

    /**
     * Displays an error message using a JFXSnackbar
     * @param errorMessage the error message to display
     */
    protected void showSnackBarError(String errorMessage) {
        JFXSnackbar snackbar = new JFXSnackbar(getRootPane());
        JFXSnackbar.SnackbarEvent snackbarEvent = new JFXSnackbar.SnackbarEvent(createSnackbarContent(errorMessage), Duration.seconds(3), null);
        snackbar.enqueue(snackbarEvent);
    }

    /**
     * Creates the content for the snackbar error message
     * @param errorMessage the error message to display
     * @return the Node representing the snackbar content
     */
    private Node createSnackbarContent(String errorMessage) {
        Node snackbarContent = ReferendumJavaFxApplication.loadFxml("snackbar-error.fxml");
        Label errorLabel = (Label) snackbarContent.lookup("#errorLabel");
        errorLabel.setText(errorMessage);

        return snackbarContent;
    }

    /**
     * Abstract method to get the root pane of the scene.
     * @return the root pane of the scene
     */
    abstract Pane getRootPane();
}
