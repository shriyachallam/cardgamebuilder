package oogasalad.runner.view.player_view;

import javafx.scene.Node;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.view.CardGroupContainer;
import oogasalad.runner.view.card_group.PlayerHandView;

/**
 * This class manages the active and inactive player views for a single player
 */
public class PlayerView {

  private final CardGroupContainer playerInHand;
  private final CardGroupContainer playerInPlay;
  private final CardGroupContainer opponentInHand;
  private final CardGroupContainer opponentInPlay;

  private final PlayerViewInterface activePlayerView;
  private final PlayerViewInterface inactivePlayerView;

  /**
   * When a new player is added this constructor creates the active and inactive player views and
   * assigns them the appropriate card group containers
   *
   * @param playerInHand   - a CardGroupContainer that contains the player's hand
   * @param playerInPlay   - a CardGroupContainer that contains the player's cards in play
   * @param opponentInHand - a CardGroupContainer that contains the opponent's hand
   * @param opponentInPlay - a CardGroupContainer that contains the opponent's cards in play
   */
  public PlayerView(CardGroupContainer playerInHand, CardGroupContainer playerInPlay,
      CardGroupContainer opponentInHand,
      CardGroupContainer opponentInPlay, ImmutablePlayer player) {
    this.playerInHand = playerInHand;
    this.playerInPlay = playerInPlay;
    this.opponentInHand = opponentInHand;
    this.opponentInPlay = opponentInPlay;
    this.activePlayerView = new ActivePlayerView(playerInHand, playerInPlay, player);
    this.inactivePlayerView = new InactivePlayerView(opponentInHand, opponentInPlay, player);
  }

  /**
   * This method returns the node for the active player view
   *
   * @return - a Node that contains the active player view
   */
  public Node getActivePlayerView() {
    return activePlayerView.getNode();
  }

  /**
   * This method returns the node for the inactive player view
   *
   * @return - a Node that contains the inactive player view
   */
  public Node getInactivePlayerView() {
    return inactivePlayerView.getNode();
  }

  /**
   * This method adds a card group to the player's hand
   *
   * @param cardGroup - a PlayerHandManager that contains card groups for both the active and
   *                  inactive views
   */
  public void addToPlayerHand(PlayerHandView cardGroup) {
    playerInHand.addCardGroup(cardGroup.getPlayerHandNode());
    opponentInHand.addCardGroup(cardGroup.getOpponentHandNode());
  }

  /**
   * This method adds a card group to the player's cards in play
   *
   * @param cardGroup - a PlayerHandManager that contains card groups for both the active and
   *                  inactive views
   */
  public void addToPlayerInPlay(PlayerHandView cardGroup) {
    playerInPlay.addCardGroup(cardGroup.getPlayerHandNode());
    opponentInPlay.addCardGroup(cardGroup.getOpponentHandNode());
  }

}
