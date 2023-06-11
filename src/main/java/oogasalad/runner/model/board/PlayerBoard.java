package oogasalad.runner.model.board;

import java.util.List;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.condition.Condition;

/**
 * Defines a game board for a card game. Can be modified to the extent that the player can make moves.
 */
public interface PlayerBoard extends ImmutableBoard {

  /**
   * returns the player actions that can currently be taken
   *
   * @return
   */
  List<PlayerAction> allowedMoves();

  /**
   * returns the condition required to end the current phase.
   * @return
   */

  List<Condition> phaseConditions();

  /**
   * selects an object on the board
   * @param selectedId
   */

  void select(int selectedId);

  /**
   * deselects an object on the board
   * @param selectedId
   */
  void deselect(int selectedId);

  void endPhase();

  void revertState();


}
