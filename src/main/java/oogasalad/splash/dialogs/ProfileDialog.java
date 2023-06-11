package oogasalad.splash.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.service.Firebase;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ProfileDialog {
  private GUIFactory f = new GUIFactory("userLogin");
  private Button login;
  private Hyperlink signup = new Hyperlink("Sign Up");;
  private Scene scene;
  private Stage stage;
  private Group root;
  private final int IMAGE_WIDTH = 400;
  private ProfileHashMap map;
  private ListView listView;
  private final int SPACING = 20;
  public static final double WIDTH = 300;
  public static final double HEIGHT = 400;

  public ProfileDialog(Firebase myFirebase) {
    this.root = new Group();
    this.scene = new Scene(root, WIDTH, HEIGHT);
    this.stage = new Stage();
    this.map = myFirebase.getUserStats();;

    initNodes();
    integrateData(this.map);
    stage.setScene(scene);
    stage.show();
  }

  private void integrateData(ProfileHashMap map) {
    for (String s : map) {
      listView.getItems().add(s);
    }
  }

  private void initNodes() {
    listView = new ListView<>();
    listView.setLayoutX(0);
    listView.setLayoutY(40);
    listView.setPrefWidth(WIDTH);
    listView.setPrefHeight(HEIGHT);
    root.getChildren().add(listView);

    Label title = new Label();
    title.setText("Player Profile: ");
    title.setStyle("-fx-font-weight: bold");
    title.setLayoutX(10);
    title.setLayoutY(10);
    root.getChildren().add(title);

    /*Label welcome = f.createLabel("Profile Page", "profile", Pos.TOP_LEFT);
    Label gamesWon = f.createLabel("Games Won", "win", Pos.TOP_LEFT);
    Label gamesLost = f.createLabel("Games Lost", "lost", Pos.TOP_LEFT);
    Label gamesMade = f.createLabel("Games Made", "made", Pos.TOP_LEFT);

    VBox mainRoot = f.formatVBox(new VBox(welcome, gamesWon, gamesLost, gamesMade), SPACING, Pos.CENTER);

    mainRoot.setPadding(new Insets(SPACING));*/
  }
  public Scene getScene(){
    return scene;
  }


}
