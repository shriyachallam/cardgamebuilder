
package oogasalad.runner.model.action.player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

/*
randomly draw a card from each card type source type to destination tag
 */
public class PlayerDraw implements PlayerAction {
  private final String sourceTag;
  private final String destinationTag;
  private final Board board;
  public PlayerDraw(Map<String, String> params, Board board) {
    this.sourceTag = params.get("source_tag");
    this.destinationTag = params.get("destination_tag");
    this.board = board;
  }

  @Override
  public void act() {
    if (board.groups().stream().filter(group -> group.isSelected()).count() != 2) {
      return;
    }
    CardGroup sourceGroup = null;
    CardGroup destinationGroup = null;

    Set<CardGroup> selectedGroups = board.groups().stream().filter(group -> group.isSelected()).collect(
        Collectors.toSet());
    for (CardGroup group: selectedGroups) {
      if (group.tag().equals(sourceTag)) {
        sourceGroup = group;
      }
      if (group.tag().equals(destinationTag)) {
        destinationGroup = group;
      }
    }
    if (sourceTag.equals("own")) {
      CardGroup hand = board.players().stream().filter(player -> player.isActive()).findFirst().get().hand();
      if (hand.isSelected()) {
        sourceGroup = hand;
      }
    } else if (destinationTag.equals("own")) {
      CardGroup hand = board.players().stream().filter(player -> player.isActive()).findFirst().get().hand();
      if (hand.isSelected()) {
        destinationGroup = hand;
      }
    }
    if (sourceGroup == null || destinationGroup == null) {
      return;
    }

    board.selectables().forEach(selectable -> selectable.deselect());
    board.transfer(sourceGroup, destinationGroup, 1);
  }

  @Override
  public TranslationPackage description() {

    return new TranslationPackage("Draw", sourceTag, destinationTag);
  }
}

