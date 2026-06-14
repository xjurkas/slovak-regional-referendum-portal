package com.oop.referendumserver.view.controllers;

import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.Viewer;
import com.oop.referendumserver.services.UserService;
import com.oop.referendumserver.services.dto.CheckLogin;
import com.oop.referendumserver.services.dto.LoginErrorEnum;
import com.oop.referendumserver.view.context.ViewContext;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

/**
 * Controller responsible for managing user sign-in functionality.
 */
@Component
public class SignInController {
    @FXML
    public MFXTextField usernameTextField;
    @FXML
    public MFXPasswordField passwordTextField;
    @FXML
    public MFXButton signInButton;
    @FXML
    public Label passwordValidationLabel;
    @FXML
    public Label usernameValidationLabel;
    @FXML
    public NavigationController navigationController;

    @Autowired
    private UserService userService;

    @Autowired
    private ViewContext context;

    /**
     * Initializes the SignInController
     * Sets event handlers for sign-in button and focuses, and listens for focus changes
     * in username and password text fields to handle validation
     */
    public void initialize() {
        signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                onSignInClicked();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // if focus is returned back to password text field to fix it
        // remove the invalid pseudo class
        passwordTextField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                unsetInvalidPassword();
            }
        });

        usernameTextField.delegateFocusedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                unsetInvalidUsername();
            }
        }));


    }

    /**
     * Handles the sign-in process when the sign-in button is clicked
     * @throws IOException if an error occurs during the sign-in process
     */
    public void onSignInClicked() throws IOException {
        CheckLogin checkLogin = userService.getUser(usernameTextField.getText(), passwordTextField.getText());

        // if log in is successful check login has user
        if (checkLogin.getAppUser() != null) {
            unsetInvalidPassword();
            unsetInvalidUsername();
            context.setCurrentUser(checkLogin.getAppUser());
            changeScene();
        } else {
            switch (checkLogin.getLoginErrorEnum()) {
                case INCORRECT_PASSWORD -> setInvalidPassword();
                case NON_EXIST -> setInvalidUsername();
            }
        }
    }

    /**
     * Changes the scene upon successful sign-in
     */
    private void changeScene() {
        ReferendumJavaFxApplication.setRoot("region-select.fxml");
    }

    /**
     * Sets the password field to an invalid state and displays an error message
     */
    private void setInvalidPassword() {
        // if incorrect password then set the password field to invalid and show error message
        passwordTextField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        passwordValidationLabel.setText("Invalid password. Try again");
        passwordValidationLabel.setVisible(true);
    }

    /**
     * Removes the invalid state from the password field and hides the error message
     */
    private void unsetInvalidPassword() {
        passwordTextField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        passwordValidationLabel.setVisible(false);
    }

    /**
     * Sets the username field to an invalid state and displays an error message
     */
    private void setInvalidUsername() {
        // if username not in database set the username field to invalid and show error message
        usernameTextField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        usernameValidationLabel.setText("User with this username does not exist");
        usernameValidationLabel.setVisible(true);
    }

    /**
     * Removes the invalid state from the username field and hides the error message.
     */
    private void unsetInvalidUsername() {
        usernameTextField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        usernameValidationLabel.setVisible(false);
    }
}
