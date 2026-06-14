package com.oop.referendumserver.view.controllers;

import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.services.ReferendumService;
import com.oop.referendumserver.view.components.ValidatedTextField;
import com.oop.referendumserver.view.context.ViewContext;
import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing the creation of referendums.
 */
@Component
public class ReferendumCreateController {
    public MFXSlider passThresholdSlider;
    public MFXSlider expectedParticipationSlider;
    public MFXSlider minLocalVotersSlider;
    public Button createReferendumButton;
    public MFXDatePicker datePicker;
    public VBox questionVbox;
    public Button createQuestionButton;
    public VBox mainDataVbox;
    @FXML
    public NavigationController navigationController;
    public Label localVoteQuotaLabel;
    public Label expectedParticipationLabel;
    @Autowired
    private ViewContext context;
    public MFXRadioButton simpleRadioButton;
    public MFXRadioButton conditionalRadioButton;
    public MFXRadioButton quotaRadioButton;

    @Autowired
    private ReferendumService referendumService;
    private ValidatedTextField validatedTitleTextField;
    private ValidatedTextField validatedDescriptionTextField;

    private static float TEXT_FIELD_WIDTH = 275.0f;

    /**
     * Initializes the referendum creation controller.
     * Sets up the UI components and event handlers.
     */
    public void initialize() throws IOException {
        context.setCurrentPage("referendum-create.fxml");

        // setup ranges of sliders
        passThresholdSlider.setMin(0);
        passThresholdSlider.setMax(1);

        expectedParticipationSlider.setMin(0);
        expectedParticipationSlider.setMax(20);

        minLocalVotersSlider.setMin(0);
        minLocalVotersSlider.setMax(1);

        // set sliders to disable
        expectedParticipationSlider.setDisable(true);
        minLocalVotersSlider.setDisable(true);

        ToggleGroup group = new ToggleGroup();
        simpleRadioButton.setToggleGroup(group);
        quotaRadioButton.setToggleGroup(group);
        conditionalRadioButton.setToggleGroup(group);

        simpleRadioButton.setOnAction(e -> {
            createSimple();
        });
        quotaRadioButton.setOnAction(e -> {
            createQuota();
        });
        conditionalRadioButton.setOnAction(e ->{
            createConditional();
        });

        // set simple radio button to selected
        simpleRadioButton.setSelected(true);

        createReferendumButton.setOnAction(e -> {
            try {
                createReferendum();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        createQuestionButton.setOnAction(e -> {
            createQuestionTextField();
        });

        // create description text field
        validatedDescriptionTextField = new ValidatedTextField(createTextField("Popis referenda"));
        mainDataVbox.getChildren().addFirst(validatedDescriptionTextField);


        // create title text field
        validatedTitleTextField = new ValidatedTextField(createTextField("Nadpis"));
        mainDataVbox.getChildren().addFirst(validatedTitleTextField);


        createQuestionTextField();

    }

    /**
     * Creates a text field with specified floating text.
     * @param floatingText The floating text to display.
     * @return The created text field.
     */

    private MFXTextField createTextField(String floatingText) {
        MFXTextField textField = new MFXTextField();
        textField.setAlignment(Pos.CENTER);
        textField.setFloatingText(floatingText);
        textField.setMaxWidth(TEXT_FIELD_WIDTH);
        textField.setPrefWidth(TEXT_FIELD_WIDTH);

        return textField;
    }

    /**
     * Event handler for the simple referendum radio button.
     * Hides unnecessary sliders and labels.
     */
    public void createSimple(){
        expectedParticipationSlider.setDisable(true);
        minLocalVotersSlider.setDisable(true);
        //expectedParticipationLabel.setVisible(false);
        //localVoteQuotaLabel.setVisible(false);
    }
    /**
     * Event handler for the quota referendum radio button.
     * Shows the expected participation slider and label, hides the local vote quota label.
     */
    public void createQuota(){
        expectedParticipationSlider.setDisable(false);
        minLocalVotersSlider.setDisable(true);
        //expectedParticipationLabel.setVisible(true);
        //localVoteQuotaLabel.setVisible(false);
    }
    /**
     * Event handler for the conditional referendum radio button.
     * Shows both the expected participation and minimum local voters sliders and labels.
     */
    public void createConditional(){
        expectedParticipationSlider.setDisable(false);
        minLocalVotersSlider.setDisable(false);
        //expectedParticipationLabel.setVisible(true);
        //localVoteQuotaLabel.setVisible(true);
    }

    /**
     * Creates a new referendum based on the input provided by the user.
     * Saves the referendum to the database and navigates to the region select view.
     */
    public void createReferendum() throws IOException {
        // validate if everything is valid with all constraints

        boolean valid = validate();
        if (!valid) {
            return;
        }

        Referendum referendum;
        if(simpleRadioButton.isSelected()) {
            referendum = new SimpleReferendum();
        }else if(quotaRadioButton.isSelected()){
            referendum = new QuotaReferendum();
        }else if (conditionalRadioButton.isSelected()){
            referendum = new ConditionalReferendum();
        }else{
            return;
        }

        Referendum initializedReferendum = initReferendum(referendum);
        referendumService.save(initializedReferendum);
        changeScene();
    }

    private Referendum initReferendum(Referendum referendum) {
        referendum.setRegion(context.getSelectedRegion());
        referendum.setTitle(validatedTitleTextField.getText());
        referendum.setDescription(validatedDescriptionTextField.getText());
        referendum.setDate(datePicker.getCurrentDate());
        List<Question> questionList = new ArrayList<>();
        for(int i = 0; i < questionVbox.getChildren().size(); i++) {
            Node node = questionVbox.getChildren().get(i);
            ValidatedTextField questionTextField = (ValidatedTextField) node;

            Question question = new Question();
            question.setText(questionTextField.getText());
            question.setId(String.valueOf(i));
            questionList.add(question);
        }
        referendum.setQuestions(questionList);

        if (referendum instanceof SimpleReferendum) {
            ((SimpleReferendum) referendum).setPassThreshold((float) passThresholdSlider.getValue());
        }

        if (referendum instanceof QuotaReferendum) {
            ((QuotaReferendum) referendum).setMinimalParticipation((long) expectedParticipationSlider.getValue());
        }

        if (referendum instanceof ConditionalReferendum) {
            ((ConditionalReferendum) referendum).setLocalVoteQuota((float) minLocalVotersSlider.getValue());
        }

        return referendum;
    }


    /**
     * Changes the scene back to the region select view.
     */
    private void changeScene() {
        ReferendumJavaFxApplication.setRoot("region-select.fxml");
    }

    /**
     * Validates all user input fields.
     * @return true if all validated fields are valid.
     */
    private boolean validate() {
        return validatedTitleTextField.validate() &
                validatedDescriptionTextField.validate() &
                validateQuestions();
    }

    /**
     * Validates all question text fields.
     * @return true if all questions are valid.
     */
    private boolean validateQuestions() {
        boolean isValid = true;
        for (Node node: questionVbox.getChildren()) {
            ValidatedTextField validatedTextField = (ValidatedTextField) node;
            if (!validatedTextField.validate()) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Creates a new text field for entering a question.
     */
    private void createQuestionTextField() {
        MFXTextField questionTextField = new MFXTextField();
        questionTextField.setAlignment(Pos.CENTER);
        questionTextField.setFloatingText("Hlasovacia otazka");
        questionTextField.setMaxWidth(275.0);
        questionTextField.setPrefWidth(275.0);

        ValidatedTextField validatedTextField = new ValidatedTextField(questionTextField);
        questionVbox.getChildren().add(validatedTextField);
    }
}
