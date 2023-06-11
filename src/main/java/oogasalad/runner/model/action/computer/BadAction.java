package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;

/**
 * --TESTING PURPOSES ONLY--
 * Computer action that throws an exception
 * Used to test the exception handling at runtime in the .step() method of the Game class
 * See GameLoopTest.java for more details (testBadBeforeAction)
 * DO NOT INCLUDE IN VALID OPITONS JSON FILE. INTERNAL USE ONLY
 * @Author Ken Kalin
 */

public class BadAction implements ComputerAction {
  private final Board board;
  public BadAction(Map<String, String> params, Board board) {
    this.board = board;
  }

  /**
   * Throws a RuntimeException returning the classname of the action
   */
  @Override
  public void act() {
   throw new RuntimeException(String.valueOf(this.getClass()));
  }

}