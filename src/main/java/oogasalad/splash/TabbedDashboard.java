package oogasalad.splash;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.frontend.Navigator;

import java.util.ResourceBundle;
import oogasalad.service.Firebase;

public class TabbedDashboard extends Scene {
    private final String Splash_CSS = "BuilderUI/CSSFiles/splash.css"; // CSS file
    private final String LANGUAGE_FOLDER = "BuilderUI/LanguageFiles/"; // Properties file
    private final int DEFAULT_PIC_LENGTH = 150;
    private final int DEFAULT_SPACING = 20;
    private static int LENGTH = 500;
    private ResourceBundle languageBundle;
    private String sceneTitle;
    private GUIFactory f;
    private Firebase myFirebase;

    public TabbedDashboard(BorderPane root, String language, Firebase myFirebase) {
        super(root, LENGTH, LENGTH);
        this.getStylesheets().add(Splash_CSS);
        this.myFirebase = myFirebase;

        languageBundle = ResourceBundle.getBundle(LANGUAGE_FOLDER + language);
        sceneTitle = languageBundle.getString("SplashStageTitle");
        f = new GUIFactory("splash");

        Label title = f.createLabel(languageBundle.getString("AppTitle"), "MainTitle", Pos.TOP_CENTER);
        title.setStyle("-fx-font-size: 20");

        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(200);
        tabPane.setPrefHeight(100);
        Tab buildTab = new Tab("Build");
        Tab serverTab = new Tab("Server");
        tabPane.getTabs().add(buildTab);
        tabPane.getTabs().add(serverTab);

        root.getChildren().add(tabPane);


        /*Label buildDesc = f.createLabel(languageBundle.getString("BuildDesc"), "buildDesc", Pos.CENTER);
        ImageView buildBtn = f.createIV(new Image("BuilderUI/ace-heart.png"), DEFAULT_PIC_LENGTH, DEFAULT_PIC_LENGTH, true);
        buildBtn.setOnMouseClicked(e -> startBuilder(language));
        VBox buildBox = f.formatVBox(new VBox(buildBtn, buildDesc), DEFAULT_SPACING, Pos.CENTER);

        Label runDesc = f.createLabel(languageBundle.getString("RunDesc"), "runDesc", Pos.CENTER);
        ImageView runBtn = f.createIV(new Image("BuilderUI/ace-spades.png"), DEFAULT_PIC_LENGTH, DEFAULT_PIC_LENGTH, true);
        runBtn.setOnMouseClicked(e -> startRunner(language));*/

        //VBox runBox = f.formatVBox(new VBox(runBtn, runDesc), DEFAULT_SPACING, Pos.CENTER);

        //HBox buildRunBox = f.formatHBox(new HBox(buildBox, runBox), DEFAULT_SPACING*3, Pos.CENTER);

        root.setTop(title);
        root.setAlignment(title, Pos.CENTER);
        //root.setCenter(buildRunBox);
    }

    /**
     * Returns the title of the scene from language properties file
     *
     * @return String title
     */
    public String getTitle() {
        if(languageBundle ==null){
            return "Not Initialized";
        }
        return sceneTitle;
    }

    /**
     * Starts the runner on a new stage
     * Allows user to play new games
     *
     * @param language
     */
    private void startRunner(String language) {
        RunnerHome scene = new RunnerHome(new BorderPane(), language, myFirebase);
        Stage stage = new Stage();
        stage.setTitle(languageBundle.getString("RunGameStage"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Starts the builder on a new stage
     * Allows user to create a new game
     *
     * @param language
     */
    private void startBuilder(String language){
        Navigator nav = new Navigator(new Stage(), language, myFirebase);
        nav.route(nav.PARAMS, languageBundle.getString("BuildGameStage"));
    }

}