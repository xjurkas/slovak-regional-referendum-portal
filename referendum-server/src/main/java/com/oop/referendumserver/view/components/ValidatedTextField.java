package com.oop.referendumserver.view.components;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

/**
 * Custom component for a validated text field with error message display
 */
public class ValidatedTextField extends VBox {

    private final MFXTextField textField;

    private final Label validationLabel;

    /**
     * Constructs a new ValidatedTextField
     * @param textField the MFXTextField component for user input
     */
    public ValidatedTextField(MFXTextField textField) {
        this.textField = textField;
        this.validationLabel = new Label();

        this.textField.setId("validatedField");


        this.validationLabel.setVisible(false);
        this.validationLabel.setId("validationLabel");
        this.validationLabel.setTextFill(Color.web("#ef6e6b"));
        this.validationLabel.setMaxWidth(200);

        initializeValidation();

        getChildren().add(this.textField);
        getChildren().add(this.validationLabel);
    }

    /**
     * Validates the text field input
     * @return true if validation succeeds, false otherwise
     */
    public boolean validate() {
        List<Constraint> constraints = textField.validate();
        if (!constraints.isEmpty()) {
            setFieldToInvalid(constraints.getFirst().getMessage());
            return false;
        }

        return true;
    }

    /**
     * Retrieves the text entered in the text field
     * @return the text entered in the text field
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Initializes the validation for the text field
     */
    private void initializeValidation() {
        textField.getValidator().constraint(createLengthConstraint(textField));
        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setFieldToValid();
            }
        });

        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                validate();
            }
        });
    }

    /**
     * Sets the text field to an invalid state and displays the validation message
     * @param message the validation message to display
     */
    private void setFieldToInvalid(String message) {
        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        validationLabel.setText(message);
        validationLabel.setVisible(true);
    }

    /**
     * Sets the text field to a valid state and hides the validation message.
     */
    private void setFieldToValid() {
        validationLabel.setVisible(false);
        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,false);
    }


    /**
     * Creates a length constraint for the text field
     * @param textField the MFXTextField component
     * @return the length constraint
     */
    private Constraint createLengthConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Field cannot be empty!")
                .setCondition(textField.textProperty().length().greaterThanOrEqualTo(1).and(textField.textProperty().isNotNull()))
                .get();
    }

}
