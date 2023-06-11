package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.player.ConcretePlayer;

/**
 * Computer callback that creates a player
 */
public class CreatePlayer implements ComputerAction {

  private final String playerName;
  private final Board board;
  public CreatePlayer(Map<String, String> params, Board board) {
    playerName = params.get("name");
    this.board = board;
  }

  @Override
  public void act() {
    board.addPlayer(new ConcretePlayer(playerName));
  }
}
