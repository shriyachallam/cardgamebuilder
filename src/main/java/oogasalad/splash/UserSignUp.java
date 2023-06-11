package oogasalad.splash;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.runner.controller.observer.Observable;

public class UserSignUp {
  private GUIFactory f = new GUIFactory("userSignUp");
  private Button signup;
  private Hyperlink loginLink = new Hyperlink("Log in");
  private Scene scene;
  private final int IMAGE_WIDTH = 400;
  private final int SPACING = 20;

  public UserSignUp(double width, double height, TextField username, PasswordField password, PasswordField confirmPassword, ComboBox<String> userRole) {
    ImageView image = f.createIV(new Image("HouseOfCards.jpg"), IMAGE_WIDTH, IMAGE_WIDTH, false);
    Label welcome = f.createLabel("Welcome to House of Cards", "welcome", Pos.CENTER);

    Label signin = f.createLabel("Please sign in to continue:", "signin", Pos.TOP_LEFT);
    username.setPrefWidth(180);
    username.setPromptText("Username");
    password.setPrefWidth(180);
    password.setPromptText("Password");
    confirmPassword.setPrefWidth(180);
    confirmPassword.setPromptText("Confirm Password");

    userRole.getItems().addAll("Member", "Non-Member");

    signup = f.createButton("Sign Up", "signup", Pos.CENTER);
    TextFlow login = new TextFlow(
        new Text("Already have an account? "), loginLink
    );

    VBox leftBox = f.formatVBox(new VBox(image, welcome), SPACING, Pos.CENTER);
    VBox rightBox = f.formatVBox(new VBox(signin, username, password, confirmPassword, userRole, signup, login), SPACING, Pos.CENTER);

    HBox mainRoot = f.formatHBox(new HBox(leftBox, rightBox), SPACING, Pos.CENTER);
    mainRoot.setPadding(new Insets(SPACING));

    scene = new Scene(mainRoot, width, height);
  }
  public Scene getScene(){
    return scene;
  }

  public Button getSignUp(){ return signup; }
  public Hyperlink getLogin() {
    return loginLink;
  }
}
