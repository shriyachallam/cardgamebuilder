package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

public abstract class GroupCondition implements Condition {

  private final String tag;
  private final Board board;

  public GroupCondition(Map<String, String> params, Board board) {
    tag = params.get("group_tag").toLowerCase();
    this.board = board;
  }

  @Override
  public boolean isValid() {
    for (CardGroup group : board.groups().stream().filter(group -> group.tag().equals(tag))
        .toArray(CardGroup[]::new)) {
      if (!isValid(group)) {
        return false;
      }
    }
    return true;
  }
  protected abstract boolean isValid(CardGroup pile);

  protected String getTag() {
    return tag;
  }

}
