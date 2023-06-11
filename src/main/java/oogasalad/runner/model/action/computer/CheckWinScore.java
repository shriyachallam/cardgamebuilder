package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;

/**
 * Computer callback that checks if the player has won the game currently hard coded to check if the
 * player's hand is empty
 */

public class CheckWinScore implements ComputerAction {
  private final Board board;
  private final int score;
  public CheckWinScore(Map<String, String> params, Board board) {
    score = Integer.parseInt(params.get("score"));
    this.board = board;
  }
  @Override
  public void act() {
    board.players().stream().filter(player -> player.getPoints() == score)
        .forEach(player -> player.setWinner(true));
  }

}