package com.oop.referendumserver.view.controllers;

import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.Notification;
import com.oop.referendumserver.db.model.Referendum;
import com.oop.referendumserver.db.model.ReferendumResult;
import com.oop.referendumserver.db.repository.NotificationRepository;
import com.oop.referendumserver.services.NotificationService;
import com.oop.referendumserver.view.FxmlLoadException;
import com.oop.referendumserver.view.context.ViewContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for managing notifications view
 */
@Component
public class NotificationController {
    @Autowired
    private ViewContext context;
    @FXML
    public ListView<VBox> notificationListView;

    @Autowired
    NotificationService notificationService;
    /**
     * Initializes the notification controller
     * Populates the notification list with the user's notifications
     */
    public void initialize() throws IOException {
        context.setCurrentPage("referendum-result-view.fxml");
        List<Notification> usersNotifications = notificationService.getNotificationsForUser(context.getCurrentUser());

        for (Notification notification : usersNotifications) {
            notificationListView.getItems().add(createListItem(notification));
        }

    }

    /**
     * Creates a list item for a notification
     * @param notification The notification object
     * @return The created list item
     * @throws IOException If an error occurs during loading of the fxml file
     */
    private VBox createListItem(Notification notification) throws IOException {

        VBox parent = (VBox) ReferendumJavaFxApplication.loadFxml("notification-view-list.fxml");

        Button checkResultButton = (Button) parent.lookup("#checkResultButton");
        checkResultButton.setOnAction(e->{
            try {
                ReferendumJavaFxApplication.setRoot("referendum-result-view.fxml");
            } catch (FxmlLoadException ex) {
                throw new RuntimeException(ex);
            }
        });

        Label notificationLabel = (Label) parent.lookup("#notificationLabel");
        notificationLabel.setText(notification.getText());

        return parent;
    }
}

