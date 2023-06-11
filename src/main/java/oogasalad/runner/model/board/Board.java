package oogasalad.runner.model.board;

import java.util.List;
import java.util.Set;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.condition.Condition;
import oogasalad.runner.model.exception.CardNotFoundException;
import oogasalad.runner.model.player.Player;

/**
 * Board interface. Defines a game board for a card game. Has access to freely modify the state of the board
 *
 * @author Delali Cudjoe
 */

public interface Board extends PlayerBoard {

  void addPlayer(Player player);

  Set<Card> cards();

  List<Player> players();
  Set<Selectable> selectables();

  void addGroup(CardGroup group);

  Set<CardGroup> groups();

  void transfer(CardGroup source, CardGroup destination, int amount);

  void moveCard(Card card, CardGroup destination) throws CardNotFoundException;


  /**
   * sets the player actions that can be accessed by getAllowedMoves
   *
   * @param actions
   */
  void setAllowedMoves(List<PlayerAction> actions);

  /**
   * sets the conditioin required to end the current phase.
   *
   * @param phaseCondition
   */
  void setPhaseConditions(List<Condition> phaseCondition);


  /**
   * Sets the description of the current phase
   * Displayed in UI above actions
   * @param phaseDescription
   */
  void setPhaseDescription(String phaseDescription);


  /**
   * returns the number of transfers that have occurred in the current phase
   *
   * @return
   */
  int getPhaseTransfers();

  /**
   * Backup functionality
   */


  /**
   * Save a backup of a valid board state
   */
  void saveBackup();


  /**
   * Revert the board state to a previous state based on the saved backup
   */
  void revertState();

  void notifyObservers();

  public void setGameStep(Runnable gameStep);
}
