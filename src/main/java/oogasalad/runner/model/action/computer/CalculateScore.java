package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;
/*
calculates score by summing the values of the current hand and adding to the players total score.
 */
public class CalculateScore implements ComputerAction {
  private final Board board;
  public CalculateScore(Map<String, String> params, Board board) {
    this.board = board;
  }
  @Override
  public void act() throws RuntimeException {
    try {
      board.players().forEach(player -> player.addPoints(player.hand().getCardScore()));
    }
    catch (Exception e) {
      throw new RuntimeException(String.valueOf(this.getClass()));
    }
  }
}
