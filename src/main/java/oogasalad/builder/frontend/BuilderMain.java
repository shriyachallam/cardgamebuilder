package oogasalad.builder.frontend;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import oogasalad.service.Firebase;
import oogasalad.splash.SplashScreen;

public class BuilderMain extends Application {
    private final String testLang = "English";
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the application to text strictly builder UI functionality with English language
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        SplashScreen splash = new SplashScreen(new BorderPane(), testLang, new Firebase());
        primaryStage.setScene(splash);
        primaryStage.show();
    }
}