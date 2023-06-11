package oogasalad.runner.model.action;

import oogasalad.runner.model.board.Board;

/**
 * Interface for actions. Actions are the things that happen in the game.
 */
@FunctionalInterface
public interface Action {

  /**
   * Performs the callback on the board
   */
  void act() throws RuntimeException;

}
