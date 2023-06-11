package oogasalad.runner.view.player_view;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.view.CardGroupContainer;
import oogasalad.runner.view.game_views.PlayerIcon;

/**
 * This class displays one active player's cards, player icon, and score
 */
public class ActivePlayerView implements PlayerViewInterface {

  private final VBox vbox = new VBox(10);
  private final HBox botPanel = new HBox(10);

  private final PlayerIconFactory iconFactory = new PlayerIconFactory();

  /**
   * This constructor creates the active player view
   *
   * @param playerInHand - a CardGroupContainer that contains the player's hand
   * @param playerInPlay - a CardGroupContainer that contains the player's cards in play
   */
  public ActivePlayerView(CardGroupContainer playerInHand, CardGroupContainer playerInPlay, ImmutablePlayer player) {
    PlayerIcon playerIcon= iconFactory.createPlayerIcon(player.name(), player.getIconId());
    botPanel.getChildren().addAll(playerIcon.getNode(), playerInHand.getNode());
    vbox.getChildren().addAll(playerInPlay.getNode(), botPanel);
  }

  @Override
  public Node getNode() {
    return vbox;
  }
}
