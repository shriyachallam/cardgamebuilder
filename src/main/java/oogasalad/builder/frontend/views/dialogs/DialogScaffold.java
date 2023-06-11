package oogasalad.builder.frontend.views.dialogs;
/**
 *
 *
 *
 * Author: T. Geissler
 *
 *
 *
 */

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class DialogScaffold {

  private final Group root;
  private final Scene scene;
  private final Stage stage;
  private double WIDTH;
  private double HEIGHT;

  public DialogScaffold(Stage primaryStage, String dialogTitle) {
    this.root = new Group();
    this.scene = new Scene(root, WIDTH, HEIGHT);
    this.stage = new Stage();

    stage.setTitle(dialogTitle);
    stage.setScene(scene);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(primaryStage);

    stage.setX(primaryStage.getX() + 100);
    stage.setY(primaryStage.getY() + 100);

    stage.show();
  }


  protected Button newButton(String txt, int x, int y) {
    Button button = new Button();
    button.setText(txt);
    button.setLayoutX(x);
    button.setLayoutY(y);
    return button;
  }
}
