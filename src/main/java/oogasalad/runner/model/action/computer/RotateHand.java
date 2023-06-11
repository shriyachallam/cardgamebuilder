package oogasalad.runner.model.action.computer;

import java.util.List;
import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

public class RotateHand implements ComputerAction{

  private int amount; // number of steps rotated
  private Board board;
  public RotateHand(Map<String, String> params, Board board) {
    this.board = board;
    amount = Integer.parseInt(params.get("amount"));
  }

  @Override
  public void act() {

  }
}
