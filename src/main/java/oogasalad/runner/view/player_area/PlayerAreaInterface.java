package oogasalad.runner.view.player_area;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * The PlayerArea is a pane where the appropriate player views are displayed. The is implemented by
 * ActivePlayerArea and Inactive Player area. Active player areas will only display one player view
 * at a time while the inactive player area will display the opponent views for all players except
 * the active player.
 */
public interface PlayerAreaInterface {

  /**
   * This method updates the player area to show the appropriate player views when the active player
   * changes
   */
  void switchPlayerView(int playerId);

  Pane getNode();

  /**
   * This method adds a player view to the player area
   *
   * @param view     - the player view to add
   * @param playerId - the id of the player that the view belongs to
   */
  void addPlayerView(Node view, int playerId);

}