package oogasalad;


import static java.util.Objects.isNull;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.runner.model.player.ConcretePlayer;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.model.player.Player;
import oogasalad.service.Firebase;
//import oogasalad.service.FirebaseService;
import oogasalad.splash.LanguagePicker;
import oogasalad.splash.SplashScreen;
import oogasalad.splash.UserLogin;
import oogasalad.splash.UserSignUp;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main extends Application {

    /**
     * A method to test (and a joke :) ).
     */
    public double getVersion () {
        return 0.001;
    }

    //use these for other panes
    public static final double WIDTH = 600;
    public static final double HEIGHT = 700;

    private Stage loginStage;
    private Stage languageStage = new Stage();
    private Stage signupStage = new Stage();
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private PasswordField confirmPassword = new PasswordField();
    private ComboBox<String> userRole = new ComboBox<>();
    private GUIFactory f = new GUIFactory("main");
    private Firebase myFirebase = new Firebase();


    /**
     * Start of the program with user login screen
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        myFirebase.initialize();

        UserLogin login = new UserLogin(HEIGHT, WIDTH, username, password);
        login.getLogin().setOnMouseClicked(this::login);
        login.getRegister().setOnAction(this::launchSignup);

        loginStage = primaryStage;
        loginStage.setTitle("User Login");
        loginStage.setScene(login.getScene());
        loginStage.show();
    }

    public void launchSignup(ActionEvent e) {
        loginStage.close();

        UserSignUp signup = new UserSignUp(HEIGHT, WIDTH, username, password, confirmPassword, userRole);
        signup.getSignUp().setOnMouseClicked(this::signup);
        signup.getLogin().setOnAction(this::launchLogin);

        signupStage.setTitle("User Sign Up");
        signupStage.setScene(signup.getScene());
        signupStage.show();
    }

    public void launchLogin(ActionEvent e) {
        signupStage.close();

        UserLogin login = new UserLogin(HEIGHT, WIDTH, username, password);
        login.getLogin().setOnMouseClicked(this::login);
        login.getRegister().setOnAction(this::launchSignup);

        loginStage.setTitle("User Login");
        loginStage.setScene(login.getScene());
        loginStage.show();
    }

    /**
     * Callback function that launches the splash screen with the given language
     *
     * @param language
     * @return
     */
    public Void launchSplash(String language) {
        SplashScreen splash = new SplashScreen(new BorderPane(), language, myFirebase);

        languageStage.setScene(splash);
        languageStage.setTitle(splash.getTitle());
        languageStage.centerOnScreen();
        languageStage.show();

        return null;
    }

    /**
     * Function that checks if the user login information is correct
     * If it is correct, it will launch the game with a language picker
     *
     * @param event
     */
    public void login(MouseEvent event) {
        if (isBlank(false)) {
            MessagePopUp blank = new MessagePopUp("Please fill in all the blanks.");
        } else if (myFirebase.login(username.getText(), password.getText())) {
            loginStage.close();

            LanguagePicker lang = new LanguagePicker(new BorderPane(), this::launchSplash);
            languageStage.setScene(lang);
            languageStage.show();
        }
    }

    public void signup(MouseEvent event){
        if(isBlank(true)){
            MessagePopUp blank = new MessagePopUp("Please fill in all the blanks.");
        } else if (myFirebase.signup(username.getText(), password.getText(), confirmPassword.getText(), userRole.getValue())) {
            signupStage.close();

            LanguagePicker lang = new LanguagePicker(new BorderPane(), this::launchSplash);
            languageStage.setScene(lang);
            languageStage.show();
        }
    }

    /**
     * Check if the account and password fields are blank
     *
     * @return boolean value
     */
    public boolean isBlank(boolean signup) {
        if (signup) {
            return username.getText().equals("") || password.getText().equals("") || confirmPassword.getText().equals("") || isNull(userRole.getValue());
        }
        else {
            return username.getText().equals("") || password.getText().equals("");
        }
    }

    /**
     * Start of the program.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
