package com.oop.referendumserver.view.controllers;

import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.services.NotificationObserver;
import com.oop.referendumserver.services.ReferendumService;
import com.oop.referendumserver.services.UserService;
import com.oop.referendumserver.services.exceptions.UserException;
import com.oop.referendumserver.view.context.ViewContext;
import io.github.palexdev.materialfx.controls.MFXCircleToggleNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller class for managing the referendum view.
 */
@Component
public class ReferendumViewController extends AbstractViewController {

    @FXML
    public ListView<VBox> referendumListView;

    @FXML
    public Label regionTitle;
    public Button createButton;
    @FXML
    public NavigationController navigationController;

    @FXML
    public AnchorPane rootPane;

    @Autowired
    private ReferendumService referendumService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationObserver notificationObserver;


    @Autowired
    private ViewContext context;

    /**
     * Initializes the referendum view controller.
     * Loads the active referendums for the selected region.
     * @throws IOException
     */
    public void initialize() throws IOException {
        // first thing to do is to set current page so go back button works properly
        context.setCurrentPage("referendum-view.fxml");
        context.setPreviousPage("region-select.fxml");

        Region selectedRegion = context.getSelectedRegion();
        regionTitle.setText(selectedRegion.getName());

        List<Referendum> referendums = referendumService.getActiveReferendumsByRegion(selectedRegion.getId());
        for (Referendum referendum : referendums) {
            referendumListView.getItems().add(createListItem(referendum));
        }

        AppUser user = context.getCurrentUser();
        if (user instanceof Admin) {
            createButton.setVisible(true);
            createButton.setOnAction(e -> {
                loadScene("referendum-create.fxml");
            });
        }
    }

    /**
     * Creates a list item for a referendum.
     * @param referendum The referendum for which the list item is created.
     * @return The VBox representing the referendum list item.
     */
    private VBox createListItem(Referendum referendum) throws IOException {
        VBox parent = (VBox) ReferendumJavaFxApplication.loadFxml("referendum-list-item.fxml");

        Label titleLabel = (Label) parent.lookup("#titleLabel");
        titleLabel.setText(referendum.getTitle());

        Label descriptionLabel = (Label) parent.lookup("#descriptionLabel");
        descriptionLabel.setText(referendum.getDescription());

        Label dateLabel = (Label) parent.lookup("#dateLabel");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        dateLabel.setText(formatter.format(referendum.getDate()));

        AppUser currentUser = context.getCurrentUser();

        if (currentUser instanceof Admin) {
            adminReferendumView(referendum, parent);
        } else if (currentUser instanceof Voter) {
            voterReferendumView(referendum, parent);
        } else if (currentUser instanceof Viewer) {
          viewerReferendumView(referendum, parent);
        }
        return parent;
    }
    /**
     * Configures the view for a viewer user type.
     */
    private void viewerReferendumView(Referendum referendum, Node parent) {
        // find participate button and disable it since viewer can't vote
        Button participateButton = (Button) parent.lookup("#participateButton");
        participateButton.setVisible(false);

        // find evaluate button and disable it since only admin can evaluate results
        Button evaluateButton = (Button) parent.lookup("#evaluateButton");
        evaluateButton.setVisible(false);

        MFXCircleToggleNode likeToggleButton = (MFXCircleToggleNode) parent.lookup("#likeToggleButton");
        if (((Viewer) context.getCurrentUser()).hasAlreadyLiked(referendum)) {
            likeToggleButton.setSelected(true);
        }

        likeToggleButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                setLikedReferendum(referendum);
            } else {
                removeLikeReferendum(referendum);
            }
        });
    }

    /**
     * Configures the view for a voter user type.
     */
    private void voterReferendumView(Referendum referendum, Node parent) {
        // find evaluate button and disable it because only admin can evaluate referendums
        Button evaluateButton = (Button) parent.lookup("#evaluateButton");
        evaluateButton.setVisible(false);

        // find participate button and make it visible so user can vote
        Button participateButton = (Button) parent.lookup("#participateButton");
        participateButton.setVisible(true);

        MFXCircleToggleNode likeToggleButton = (MFXCircleToggleNode) parent.lookup("#likeToggleButton");
        if (((Voter) context.getCurrentUser()).hasAlreadyLiked(referendum)) {
            likeToggleButton.setSelected(true);
        }
        likeToggleButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                setLikedReferendum(referendum);
            } else {
                removeLikeReferendum(referendum);
            }
        });

        // set action for participate button
        participateButton.setOnAction(e -> {
            // check if user can vote on this referendum
            Voter voter = (Voter) context.getCurrentUser();

            if (voter.hasAlreadyVoted(referendum)) {
                showSnackBarError("Za toto referendum už ste hlasovali");
            } else {
                context.setSelectedReferendum(referendum);
                loadScene("referendum-form-view.fxml");
            }
        });
    }

    /**
     * Configures the view for an admin user type.
     */
    private void adminReferendumView(Referendum referendum, Node parent) {
        // find participate button and disable it since admin can't vote
        Button participateButton = (Button) parent.lookup("#participateButton");
        participateButton.setVisible(false);

        // find evaluate button and make it visible so admin can evaluate results
        Button evaluateButton = (Button) parent.lookup("#evaluateButton");

        MFXCircleToggleNode likeToggleButton = (MFXCircleToggleNode) parent.lookup("#likeToggleButton");
        likeToggleButton.setVisible(false);

        // set action for evaluate button
        evaluateButton.setOnAction(e -> {
            // evaluate referendum
            referendumService.evaluate(referendum);

            // update expiration date of referendum to yesterday
            referendum.setDate(LocalDate.now().minusDays(1));
            Referendum updatedReferendum = referendumService.save(referendum);

            // change scene to the results of the referendum
            context.setSelectedReferendum(updatedReferendum);
            loadScene("referendum-result-view.fxml");
        });
    }

    /**
     * Adds the liked referendum to the user's profile.
     * @param referendum The referendum to be liked.
     */
    public void setLikedReferendum(Referendum referendum){
        AppUser user = context.getCurrentUser();

        try {
            AppUser updatedUser = userService.addLikedReferendumToUser(user, referendum);
            context.setCurrentUser(updatedUser);

            notificationObserver.subscribe((Viewer) user, referendum);

        } catch (UserException e) {
            showSnackBarError(e.getMessage());
        }

    }
    /**
     * Removes the liked referendum from the user's profile.
     * @param referendum The referendum to be unliked.
     */
    public void removeLikeReferendum(Referendum referendum){
        AppUser user = context.getCurrentUser();

        try {
            AppUser updatedUser = userService.removeLikedReferendumToUser(user, referendum);
            context.setCurrentUser(updatedUser);

            notificationObserver.unsubscribe((Viewer) user, referendum);
        } catch (UserException e) {
            showSnackBarError(e.getMessage());
        }
    }

    /**
     * Returns the root pane of the controller.
     * @return The root pane.
     */
    @Override
    Pane getRootPane() {
        return rootPane;
    }
}
