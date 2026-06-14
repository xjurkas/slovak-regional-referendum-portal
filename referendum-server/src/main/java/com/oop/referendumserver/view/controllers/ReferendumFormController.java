package com.oop.referendumserver.view.controllers;


import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.services.ReferendumFormService;
import com.oop.referendumserver.services.UserService;
import com.oop.referendumserver.services.exceptions.UserException;
import com.oop.referendumserver.view.FxmlLoadException;
import com.oop.referendumserver.view.context.ViewContext;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing the referendum form view.
 */
@Component
public class ReferendumFormController extends AbstractViewController {
    @FXML
    public AnchorPane rootPane;

    @Autowired
    private ReferendumFormService referendumFormService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewContext context;
    @FXML
    public Label referendumTitle;
    @FXML
    private ListView<VBox> questionListView;
    @FXML
    public NavigationController navigationController;
    /**
     * Initializes the referendum form controller.
     * Displays the selected referendum's title and loads its questions into the list view.
     */
    public void initialize() throws IOException {
        context.setCurrentPage("referendum-form-view");
        Referendum referendum = context.getSelectedReferendum();
        referendumTitle.setText(referendum.getTitle());
        List<Question> questions = referendum.getQuestions();
        for (Question question : questions) {
            questionListView.getItems().add(createListItem(question));
        }
    }

    /**
     * Creates a list item for a question.
     * @param question The question for which the list item is created.
     * @return The VBox representing the question list item.
     */
    private VBox createListItem(Question question) throws IOException {
        VBox parent = (VBox) ReferendumJavaFxApplication.loadFxml("referendum-question-item.fxml");

        parent.setId(question.getId());

        Label questionLabel = (Label) parent.lookup("#questionLabel");
        questionLabel.setText(question.getText());

        RadioButton yesRadioButton = (RadioButton) parent.lookup("#yesButton");
        RadioButton noRadioButton = (RadioButton) parent.lookup("#noButton");

        ToggleGroup group = new ToggleGroup();
        yesRadioButton.setToggleGroup(group);
        noRadioButton.setToggleGroup(group);

        return parent;
    }

    /**
     * Handles the event of sending the vote for the referendum form.
     */
    @FXML
    private void sendVote() {
        Voter voter = (Voter) context.getCurrentUser();
        Referendum referendum = context.getSelectedReferendum();

        // register vote for the user
        try {
            // update user with voted referendum
            AppUser updatedUser = userService.addVotedReferendumToUser(voter, referendum);
            context.setCurrentUser(updatedUser);

        } catch (UserException ex) {
            showSnackBarError(ex.getMessage());
        }

        ReferendumForm referendumForm = new ReferendumForm();
        Map<String, Boolean> questionAnswers = new HashMap<>();

        for (VBox questionVbox : questionListView.getItems()) {
            RadioButton yesButton = (RadioButton) questionVbox.lookup("#yesButton");
            if (yesButton.isSelected()) {
                questionAnswers.put(questionVbox.getId(), true);
            } else {
                questionAnswers.put(questionVbox.getId(), false);
            }
        }
        referendumForm.setReferendum(referendum);
        referendumForm.setQuestionAnswers(questionAnswers);
        referendumForm.setVoter(voter);

        // save to db
        referendumFormService.saveToDb(referendumForm);

        // remove referendum and region from context
        context.setSelectedReferendum(null);
        context.setSelectedRegion(null);

        // change scene
        loadScene("region-select.fxml");
    }

    /**
     * Retrieves the root pane of the view.
     * @return The root pane.
     */
    @Override
    Pane getRootPane() {
        return rootPane;
    }
}
