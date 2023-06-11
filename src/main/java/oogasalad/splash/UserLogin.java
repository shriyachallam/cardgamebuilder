package oogasalad.splash;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class UserLogin {
    private GUIFactory f = new GUIFactory("userLogin");
    private Button login;
    private Hyperlink signup = new Hyperlink("Sign Up");;
    private Scene scene;
    private final int IMAGE_WIDTH = 400;
    private final int SPACING = 20;

    public UserLogin(double width, double height, TextField username, PasswordField password) {
        ImageView image = f.createIV(new Image("HouseOfCards.jpg"), IMAGE_WIDTH, IMAGE_WIDTH, false);
        Label welcome = f.createLabel("Welcome to House of Cards", "welcome", Pos.CENTER);

        Label signin = f.createLabel("Please sign in to continue:", "signin", Pos.TOP_LEFT);
        username.setPrefWidth(180);
        username.setPromptText("Username");
        password.setPrefWidth(180);
        password.setPromptText("Password");

        login = f.createButton("Login", "login", Pos.CENTER);
        TextFlow register = new TextFlow(
            new Text("Don't have an account? "), signup
        );

        VBox leftBox = f.formatVBox(new VBox(image, welcome), SPACING, Pos.CENTER);
        VBox rightBox = f.formatVBox(new VBox(signin, username, password, login, register), SPACING, Pos.CENTER);

        HBox mainRoot = f.formatHBox(new HBox(leftBox, rightBox), SPACING, Pos.CENTER);
        mainRoot.setPadding(new Insets(SPACING));

        scene = new Scene(mainRoot, width, height);
    }
    public Scene getScene(){
        return scene;
    }

    public Button getLogin(){ return login; }
    public Hyperlink getRegister() {
        return signup;
    }
}
