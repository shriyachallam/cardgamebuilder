package oogasalad.runner.model.action.computer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

/**
 * Computer callback that transfers cards from one group to another
 */
public class Transfer implements ComputerAction {
  private final String sourceTag;
  private final String destinationTag;
  private final int amount;
  private final Board board;
  public Transfer(Map<String, String> params, Board board) {
    this.sourceTag = params.get("source_tag");
    this.destinationTag = params.get("destination_tag");
    this.amount = Integer.parseInt(params.get("amount"));
    this.board = board;
  }
  public void act() {
    Set<CardGroup> sourceGroups = board.groups().stream()
        .filter(group -> group.tag().equals(sourceTag)).collect(Collectors.toSet());
    Set<CardGroup> destinationGroups = board.groups().stream()
        .filter(group -> group.tag().equals(destinationTag)).collect(Collectors.toSet());
    for (CardGroup sourceGroup : sourceGroups) {
      for (CardGroup destinationGroup : destinationGroups) {
        board.transfer(sourceGroup, destinationGroup, amount);
      }
    }
  }
}
