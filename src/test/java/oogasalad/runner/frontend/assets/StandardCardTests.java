package oogasalad.runner.frontend.assets;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author alecliu The StandardCardTests class is purely an experimental class to look into
 * different card sizings and placements.
 */
public class StandardCardTests extends Application {

  private static final String CARD_RESOURCE_PATH = "/oogasalad/runner/cardIcons/cardImages/%s.png";
  private final Stage stage;
  private final Group root;
  private final Scene scene;

  public StandardCardTests() {
    stage = new Stage();
    root = new Group();
    scene = new Scene(root, 800, 800);
    scene.setFill(Color.GREEN);
    stage.setScene(scene);
    stage.show();
    testCardRender();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    StandardCardTests standardCardTests = new StandardCardTests();
  }

  void testCardRender() {
    // TOP IMAGE
    ImageView imgTOP = setupCard("2_of_clubs");
    imgTOP.setX(100);
    imgTOP.setY(100);
    imgTOP.setFitWidth(50);
    imgTOP.setFitHeight(75);

    Text text1 = new Text("50x75 (Width x Height)");
    text1.setX(200);
    text1.setY(150);

    // MIDDLE IMAGE
    ImageView imgMID = setupCard("ace_of_spades");
    imgMID.setX(100);
    imgMID.setY(300);
    imgMID.setFitWidth(75);
    imgMID.setFitHeight(120);

    Text text2 = new Text("75x120 (Width x Height)");
    text2.setX(225);
    text2.setY(350);

    // TOP IMAGE
    ImageView imgBOTTOM = setupCard("king_of_hearts2");
    imgBOTTOM.setX(100);
    imgBOTTOM.setY(500);
    imgBOTTOM.setFitWidth(100);
    imgBOTTOM.setFitHeight(150);

    Text text3 = new Text("100x150 (Width x Height)");
    text3.setX(250);
    text3.setY(550);

    root.getChildren().addAll(imgTOP, imgMID, imgBOTTOM, text1, text2, text3);
  }

  /**
   * Formats a ImageView object based upon the card name given.
   * @param cardName card name, separated by underscores (ex: 2_of_clubs)
   * @return formatted ImageView object
   */
  private ImageView setupCard(String cardName) {
    return new ImageView(
        new Image(getClass().getResourceAsStream(String.format(CARD_RESOURCE_PATH, cardName))));
  }

  public static void main(String[] args) {
    launch(args);
  }
}
