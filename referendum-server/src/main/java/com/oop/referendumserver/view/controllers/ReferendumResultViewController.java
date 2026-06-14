package com.oop.referendumserver.view.controllers;
import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.services.ReferendumService;
import com.oop.referendumserver.services.RegionService;
import com.oop.referendumserver.view.context.ViewContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Ref;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * Controller class for managing the referendum result view.
 */
@Component
public class ReferendumResultViewController {
    public javafx.scene.control.Label titleLabel;
    public javafx.scene.control.Label descriptionLabel;
    public javafx.scene.control.Label resultLabel;
    public javafx.scene.control.Label totalParticipationLabel;
    @FXML
    public NavigationController navigationController;
    @FXML
    public ListView<VBox> resultListView;
    public ComboBox<Region> regionSelectComboBox;

    @Autowired
    private ViewContext context;
    @Autowired
    private ReferendumService referendumService;
    @Autowired
    private RegionService regionService;
    /**
     * Initializes the referendum result view controller.
     * Loads the regions into the region selection combo box.
     */
    public void initialize() throws IOException {
        context.setCurrentPage("referendum-result-view.fxml");

        regionSelectComboBox.setItems(FXCollections.observableArrayList(regionService.getAllRegions()));

    }
    /**
     * Handles the event of selecting a region from the combo box.
     * Retrieves and displays the referendums results for the selected region.
     */
    public void handleRegionSelection(javafx.event.ActionEvent actionEvent) throws IOException {
        Region selectedRegion = regionSelectComboBox.getValue();
        resultListView.getItems().clear();
        if (selectedRegion != null) {
            String selectedRegionId = selectedRegion.getId();
            List<Referendum> referendums = referendumService.getExpiredReferendumsByRegion(selectedRegionId);
            for (Referendum referendum : referendums) {
                resultListView.getItems().add(createListItem(referendum));
            }
        }
    }
    /**
     * Creates a list item for a referendum.
     * @param referendum The referendum for which the list item is created.
     * @return The VBox representing the referendum result list item.
     */
    private VBox createListItem(Referendum referendum) throws IOException {

        VBox parent = (VBox) ReferendumJavaFxApplication.loadFxml("referendum-result-list-view.fxml");

        resultListView.getItems().clear();

        ReferendumResult referendumResult = referendumService.getResultByReferendum(referendum);

        Label titleLabel = (Label) parent.lookup("#titleLabel");
        titleLabel.setText(referendum.getTitle());

        Label descriptionLabel = (Label) parent.lookup("#descriptionLabel");
        descriptionLabel.setText(referendum.getDescription());

        Label resultLabel = (Label) parent.lookup("#resultLabel");
        resultLabel.setText("Výsledok: " + referendumResult.getResult().toString());

        Label totalParticipationLabel = (Label) parent.lookup("#totalParticipationLabel");
        totalParticipationLabel.setText("Počet zúčastneních: " + referendumResult.getNumberOfParticipants().toString());

        Label numOfYesLabel = (Label) parent.lookup("#numOfYesLabel");
        numOfYesLabel.setText(referendumResult.getNumberOfYesVotes().toString() + " občanov hlasovalo za áno");

        return parent;
    }
}
