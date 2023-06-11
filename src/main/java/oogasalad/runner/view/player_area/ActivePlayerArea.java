package oogasalad.runner.view.player_area;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This class contains all of the active player views and will switch between them when the active
 * player changes
 */
public class ActivePlayerArea implements PlayerAreaInterface {

  HashMap<Integer, Node> playerToViewMap = new HashMap<>();

  int firstPlayer;
  Pane pane;

  public ActivePlayerArea() {
    pane = new VBox();
    pane.setId("playerPane");
  }

  @Override
  public Pane getNode() {
    return pane;
  }

  /**
   * This method adds a player view to the active player area
   *
   * @param view     - the player view to add
   * @param playerId - the id of the player that the view belongs to
   */
  @Override
  public void addPlayerView(Node view, int playerId) {
    if (playerToViewMap.isEmpty()) {
      firstPlayer = playerId;
    }
    playerToViewMap.put(playerId, view);
    switchPlayerView(firstPlayer);
  }

  /**
   * This method switches the active player view to the view of the player with the given id
   *
   * @param playerId - the id of the player to switch to
   */
  @Override
  public void switchPlayerView(int playerId) {
    pane.getChildren().clear();
    pane.getChildren().add(playerToViewMap.get(playerId));
  }

}