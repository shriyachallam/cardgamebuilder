package oogasalad.splash;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.builder.frontend.Navigator;
import oogasalad.builder.frontend.GUIFactory;

import java.util.ResourceBundle;
import oogasalad.service.Firebase;
import oogasalad.splash.dialogs.ProfileDialog;

import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;

public class SplashScreen extends Scene {
    private final String Splash_CSS = "BuilderUI/CSSFiles/splash.css"; // CSS file
    private final int DEFAULT_PIC_LENGTH = 150;
    private final int DEFAULT_SPACING = 20;
    private static int LENGTH = 500;
    private Translator translator;
    private String sceneTitle;
    private GUIFactory f;
    private Firebase myFirebase;

    public SplashScreen(BorderPane root, String language, Firebase myFirebase) {
        super(root, LENGTH, LENGTH);
        this.getStylesheets().add(Splash_CSS);
        myFirebase.getGames();

        translator = new DefaultTranslator(AppType.BUILDER, language);
        //languageBundle = ResourceBundle.getBundle(LANGUAGE_FOLDER + language);
        sceneTitle = translator.translateToUserSpace("SplashStageTitle");
        f = new GUIFactory("splash");

        this.myFirebase = myFirebase;

        Label title = f.createLabel(translator.translateToUserSpace("AppTitle"), "MainTitle", Pos.CENTER);

        Label buildDesc = f.createLabel(translator.translateToUserSpace("BuildDesc"), "buildDesc", Pos.CENTER);
        ImageView buildBtn = f.createIV(new Image("BuilderUI/ace-heart.png"), DEFAULT_PIC_LENGTH, DEFAULT_PIC_LENGTH, true);
        buildBtn.setOnMouseClicked(e -> startBuilder(language));
        VBox buildBox = f.formatVBox(new VBox(buildBtn, buildDesc), DEFAULT_SPACING, Pos.CENTER);

        Label runDesc = f.createLabel(translator.translateToUserSpace("RunDesc"), "runDesc", Pos.CENTER);
        ImageView runBtn = f.createIV(new Image("BuilderUI/ace-spades.png"), DEFAULT_PIC_LENGTH, DEFAULT_PIC_LENGTH, true);
        runBtn.setOnMouseClicked(e -> startRunner(language));

        Button profileButton = f.createButton("My Profile", "profButton", Pos.TOP_RIGHT);
        profileButton.setOnAction(t -> {
            ProfileDialog profileDialog = new ProfileDialog(myFirebase);
        });

        VBox runBox = f.formatVBox(new VBox(profileButton, runBtn, runDesc), DEFAULT_SPACING, Pos.CENTER);
        HBox buildRunBox = f.formatHBox(new HBox(buildBox, runBox), DEFAULT_SPACING*3, Pos.CENTER);

        root.setTop(title);
        root.setAlignment(title, Pos.CENTER);
        root.setCenter(buildRunBox);
    }

    /**
     * Returns the title of the scene from language properties file
     *
     * @return String title
     */
    public String getTitle() {
        if(translator ==null){
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
        stage.setTitle(translator.translateToUserSpace("RunGameStage"));
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
        if (myFirebase.hasBuilderAccess()) {
          Navigator nav = new Navigator(new Stage(), language, myFirebase);
          nav.route(nav.PARAMS, translator.translateToUserSpace("BuildGameStage"));
        }
    }

}
