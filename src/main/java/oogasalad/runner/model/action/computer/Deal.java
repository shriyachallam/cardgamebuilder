package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;
/*
given a source tag, it distributes 1 card from each cardgroup of source tag type to each players hand
 */
public class Deal implements ComputerAction {
  private final String sourceTag;
  private final Board board;
  private final int amount;
  public Deal(Map<String, String> params, Board board) {
    this.board = board;
    this.amount = Integer.parseInt(params.get("amount"));
    this.sourceTag = params.get("source_tag");
  }

  @Override
  public void act() {
    board.groups().stream().filter(group -> group.tag().equals(sourceTag)).forEach(group -> {
      for (int i=0; i<amount; i++) {
        board.players().forEach(player -> player.hand().add(group.draw()));
      }
    });
  }

}
