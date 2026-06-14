package com.oop.referendumserver.view.controllers;

import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.view.FxmlLoadException;
import com.oop.referendumserver.view.context.ViewContext;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Controller class for managing navigation within the application
 */
@Component
public class NavigationController {
    public MFXButton mainPageButton;
    public MFXButton signOutButton;
    public MFXButton goBackButton;
    public MFXButton notificationButton;
    public MFXButton resultViewButton;
    public AnchorPane rootPane;

    @Autowired
    private ViewContext context;

    /**
     * Initializes the navigation controller
     * Defines actions for button clicks
     */
    public void initialize() {

        goBackButton.setOnAction(e ->{
            try {
                ReferendumJavaFxApplication.setRoot(context.getPreviousPage());
            } catch (FxmlLoadException ex) {
                throw new RuntimeException(ex);
            }
        });

        mainPageButton.setOnAction(e ->{
            try {
                ReferendumJavaFxApplication.setRoot("region-select.fxml");
            } catch (FxmlLoadException ex) {
                throw new RuntimeException(ex);
            }
        });

        signOutButton.setOnAction(e ->{
            try {
                context.cleanUp();
                ReferendumJavaFxApplication.setRoot("sign-in-view.fxml");
            } catch (FxmlLoadException ex) {
                throw new RuntimeException(ex);
            }
        });

        resultViewButton.setOnAction(e ->{
            try {
                ReferendumJavaFxApplication.setRoot("referendum-result-view.fxml");
            } catch (FxmlLoadException ex) {
                throw new RuntimeException(ex);
            }
        });
        notificationButton.setOnAction(e ->{
            try {
                ReferendumJavaFxApplication.setRoot("notification-view.fxml");
            } catch (FxmlLoadException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Hides the main page button.
     */
    public void hideMainPageButton() {
        mainPageButton.setVisible(false);
    }
    /**
     * Hides the go back button.
     */
    public void hideGoBackButton(){
        goBackButton.setVisible(false);
    }

    /**
     * Hides the sign out button.
     */
    public void hideSignOutButton(){
        signOutButton.setVisible(false);
    }

}
