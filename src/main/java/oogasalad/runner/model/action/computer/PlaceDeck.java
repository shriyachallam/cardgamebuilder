package oogasalad.runner.model.action.computer;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.StandardCard;

/**
 * Computer callback that places a deck of cards into a group
 */
public class PlaceDeck implements ComputerAction {

  private final String tag;
  private final Board board;

  public PlaceDeck(Map<String, String> params, Board board) {
    tag = params.get("group_tag");
    this.board = board;
  }

  @Override
  public void act() {
    board.groups().stream().filter(group -> group.tag().equals(tag))
        .forEach(group -> StandardCard.all().forEach(group::add));
  }
}
