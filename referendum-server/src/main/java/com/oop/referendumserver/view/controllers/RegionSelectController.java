package com.oop.referendumserver.view.controllers;

import com.oop.referendumserver.ReferendumJavaFxApplication;
import com.oop.referendumserver.db.model.Admin;
import com.oop.referendumserver.db.model.Region;
import com.oop.referendumserver.services.RegionService;
import com.oop.referendumserver.services.UserService;
import com.oop.referendumserver.tools.ResourceReader;
import com.oop.referendumserver.view.components.RegionSVGPath;
import com.oop.referendumserver.view.context.ViewContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * Controller responsible for managing the selection of regions on the map,
 * displaying region descriptions, and navigating to the corresponding referendums.
 */
@Component
public class RegionSelectController {
    @FXML
    public Label errorLabel;
    @FXML
    public Pane mainPane;
    @FXML
    public TextFlow descriptionTextFlow;
    @FXML
    public Button adminScene;

    @FXML
    public Label regionTitle;
    @FXML
    public VBox descriptionVbox;

    @FXML
    public VBox regionTitleVbox;

    @Autowired
    private RegionService regionService;

    @Autowired
    private ViewContext context;

    @Autowired
    private ResourceReader resourceReader;

    private Map<String, RegionSVGPath> regionSVGPathMap;

    private EventHandler<MouseEvent> handler;

    @FXML
    public NavigationController navigationController;


    /**
     * Initializes the RegionSelectController
     * Sets the current page, hides navigation buttons, loads region data
     * initializes map graphics, and sets up event handling for region selection
     * @throws IOException if an error occurs while initializing the controller
     */
    public void initialize() throws IOException {
        // first thing to do is to set current page so go back button works properly
        context.setCurrentPage("region-select.fxml");
        navigationController.hideMainPageButton();
        navigationController.hideGoBackButton();


        if(Objects.equals(context.getPreviousPage(), "referendum-form-view.fxml")){
            context.setPreviousPage("referendum-view.fxml");
        }
        if(Objects.equals(context.getPreviousPage(), "referendum-create.fxml")){
            context.setPreviousPage("referendum-view.fxml");
        }
        // get all regions from db
        List<Region> regions = regionService.getAllRegions();

        // load region graphic properties
        Properties regionProperties = resourceReader.readProperties("region.properties");

        regionSVGPathMap = createRegionPaths(regions, regionProperties);
        handler = createEventHandler();
        initializeMapGraphics();
    }
    /**
     * Changes the scene to the referendums view for the selected region
     * @param region The selected region
     * @throws IOException if an error occurs while changing the scene
     */
    public void changeSceneToRegionReferendums(Region region) throws IOException {
        context.setSelectedRegion(region);
        ReferendumJavaFxApplication.setRoot("referendum-view.fxml");
    }
    /**
     * Updates the region description UI based on the selected region
     * @param regionSVGPath The RegionSVGPath representing the selected region
     */
    public void changeRegionDescription(RegionSVGPath regionSVGPath) {
        Region region = regionSVGPath.getRegion();

        // make vbox visible
        descriptionVbox.setVisible(true);

        // set region title
        regionTitle.setText(region.getName());

        // change region title color to hoover color
        String regionHooverColor = regionSVGPath.getHooverColorHex();
        regionTitleVbox.setStyle("-fx-background-color: " + regionHooverColor);

        // clear description children
        descriptionTextFlow.getChildren().clear();

        // iterate over each description text of the region
        List<Text> texts = new ArrayList<>();
        for (String descriptionText: region.getDescription()) {
            Text text;
            if (descriptionText.startsWith("<b>")) {
                // remove prefix from text
                text = new Text(descriptionText.substring(3));
                text.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
            } else {
                text = new Text(descriptionText);
                text.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
            }

            texts.add(text);
            texts.add(new Text("\n")); // add new line after each description
        }
        descriptionTextFlow.getChildren().addAll(texts);
    }

    /**
     * Initializes the graphics for displaying regions on the map
     */
    private void initializeMapGraphics() {
        regionSVGPathMap.forEach((name, path) -> {
            path.setOnMouseEntered(new WeakEventHandler<>(handler));
            path.setOnMouseExited(new WeakEventHandler<>(handler));
            path.setOnMouseClicked(new WeakEventHandler<>(handler));

            // add as first so its at the top of the map
            mainPane.getChildren().addFirst(path);
        });
    }

    /**
     * Creates an event handler for mouse events on the map regions
     * @return The created EventHandler for mouse events
     */
    private EventHandler<MouseEvent> createEventHandler() {
        return event -> {
            RegionSVGPath regionPath = (RegionSVGPath) event.getSource();

            // svg path will color itself based on the mouse event
            regionPath.handleEvent(event);

            // if mouse entered region set region description data
            if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
                changeRegionDescription(regionPath);
            }

            // if mouse exited region clear out description data from UI
            if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
                clearRegionDescription();
            }

            // if mouse clicked on region change scene to referendums
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                try {
                    changeSceneToRegionReferendums(regionPath.getRegion());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Creates RegionSVGPath objects representing map regions based on region data
     * @param regions          The list of regions to create SVG paths for
     * @param regionProperties The properties containing region path, fill color, and hoover color
     * @return A map of region IDs to RegionSVGPath objects
     */
    private Map<String, RegionSVGPath> createRegionPaths(List<Region> regions, Properties regionProperties) {
        Map<String, RegionSVGPath> regionPaths = new HashMap<>();

        regions.forEach((region -> {
            String regionId = region.getId();
            String path = regionProperties.getProperty(regionId + ".path");
            String fillColor = regionProperties.getProperty(regionId + ".fillColor");
            String hooverColor = regionProperties.getProperty(regionId + ".hooverColor");

            RegionSVGPath svgPath = new RegionSVGPath(region, path, Color.web(fillColor, 0.75), Color.web(hooverColor));
            regionPaths.put(regionId, svgPath);
        }));

        return regionPaths;
    }

    /**
     * Clears the region description UI
     */
    private void clearRegionDescription() {
        descriptionVbox.setVisible(false);
        regionTitle.setText("");
        descriptionTextFlow.getChildren().clear();
    }
}
