package oogasalad.runner.view.player_view;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.view.CardGroupContainer;
import oogasalad.runner.view.game_views.PlayerIcon;

/**
 * This class displays one inactive player's cards, player icon, and score
 */
public class InactivePlayerView implements PlayerViewInterface {

  private final VBox cards = new VBox(10);
  private final HBox hbox = new HBox(10);
  private final PlayerIconFactory iconFactory = new PlayerIconFactory();

  /**
   * This constructor creates the inactive player view
   *
   * @param opponentInHand - a CardGroupContainer that contains the inactive player's hand
   * @param opponentInPlay - a CardGroupContainer that contains the inactive player's cards in play
   */
  public InactivePlayerView(CardGroupContainer opponentInHand, CardGroupContainer opponentInPlay,
      ImmutablePlayer player ) {
    cards.getChildren().addAll(opponentInHand.getNode(), opponentInPlay.getNode());
    PlayerIcon playerIcon= iconFactory.createPlayerIcon(player.name(), player.getIconId());
    hbox.getChildren().addAll(playerIcon.getNode(), cards);
  }

  /**
   * This method returns the node for the inactive player view
   *
   * @return - a Node that contains the inactive player view
   */
  public Node getNode() {
    return hbox;
  }

}
