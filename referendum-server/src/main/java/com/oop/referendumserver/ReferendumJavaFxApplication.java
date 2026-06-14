package com.oop.referendumserver;

import com.oop.referendumserver.tools.YamlPropertyLoaderFactory;
import com.oop.referendumserver.view.FxmlLoadException;
import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * The main entry point for the Referendum Server application.
 * Extends JavaFX Application and integrates with Spring Boot.
 */
@SpringBootApplication
@PropertySource(value = "classpath:data.yml", factory = YamlPropertyLoaderFactory.class)
@ConfigurationPropertiesScan
public class ReferendumJavaFxApplication extends Application {
    private static ConfigurableApplicationContext context;
    private Parent rootNode;

    private static Scene scene;

    /**
     * Loads an FXML file with a Spring-managed controller
     * @param fxml The path to the FXML file
     * @return The root node of the loaded FXML file
     * @throws FxmlLoadException If an unexpected error occurs during loading of the FXML file
     */
    public static Parent loadFxml(String fxml) throws FxmlLoadException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ReferendumJavaFxApplication.class.getResource(fxml));
            fxmlLoader.setControllerFactory(context::getBean);
            return fxmlLoader.load();
        } catch (IOException ex) {
            throw new FxmlLoadException();
        }

    }

    /**
     * Sets the root scene with the specified FXML file
     * @param fxml The path to the FXML file
     * @throws FxmlLoadException If an unexpected error occurs during loading of the FXML file
     */
    public static void setRoot(String fxml) throws FxmlLoadException {
        scene.setRoot(loadFxml(fxml));
    }

    /**
     * Initializes the JavaFX application, Spring Boot context, CSSFX, and MaterialFX theming.
     * @throws Exception if an error occurs during initialization.
     */
    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ReferendumJavaFxApplication.class);
        builder.application().setWebApplicationType(WebApplicationType.NONE);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));

        CSSFX.start();

        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        rootNode = loadFxml("sign-in-view.fxml");
    }

    /**
     * Starts the JavaFX application and sets up the primary stage.
     * @param stage the primary stage for this application.
     * @throws IOException if an error occurs while loading the FXML file or setting the scene.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        scene = new Scene(rootNode, visualBounds.getWidth(), visualBounds.getHeight());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Stops the JavaFX application and performs cleanup tasks.
     * @throws Exception if an error occurs during shutdown.
     */
    @Override
    public void stop() throws Exception {
        context.close();
    }

    /**
     * Main method to launch the JavaFX application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
