package oogasalad.runner.view.game_views;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * This class creates a player icon that is displayed in the player areas to associate the player
 * with the cards they have and who the active player is
 */
public class PlayerIcon {

  VBox vbox = new VBox(10);
  ImageView playerIcon;

  /**
   * This class creates a player icon that is displayed in the player areas to associate the player
   * with the cards they have and who the active player is
   */
  public PlayerIcon(String playerName, ImageView playerIcon) {
    this(playerIcon);
    playerIcon.setId(playerName+"playerIcon");
    Node name = new Label(playerName);
    vbox.getChildren().add(name);
  }
  public PlayerIcon(ImageView playerIcon) {
    this.playerIcon = playerIcon;
    vbox.getChildren().add(playerIcon);
    vbox.alignmentProperty().set(javafx.geometry.Pos.CENTER);
    vbox.setPadding(new javafx.geometry.Insets(10));
  }

  public Node getNode() {
    return vbox;
  }


  public Image getImage() {
    return playerIcon.getImage();
  }
}
