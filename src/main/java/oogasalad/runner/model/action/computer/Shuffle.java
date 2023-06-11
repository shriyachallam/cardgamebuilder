package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;
/*
given a group tag, shuffle the cards in each group of that tag
 */
public class Shuffle implements ComputerAction {
  private final Board board;
  private final String tag;
  public Shuffle(Map<String, String> params, Board board) {
    this.board = board;
    tag = params.get("group_tag").toLowerCase();
  }

  @Override
  public void act() {
    board.groups().stream().filter(group -> group.tag().equals(tag)).forEach(group -> group.shuffle());
  }

}
