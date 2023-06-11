package oogasalad.runner.model.action.player;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
/*
given a selected card, tranfer it to the destination tag
 */
public class PlayerTransfer implements PlayerAction {
  private final String sourceTag;
  private final String destinationTag;
  private final Board board;
  public PlayerTransfer(Map<String, String> params, Board board) {
    this.sourceTag = params.get("source_tag");
    this.destinationTag = params.get("destination_tag");
    this.board = board;
  }

  @Override
  public void act() {
    if (board.cards().stream().filter(card -> card.isSelected()).count() != 1
    || board.groups().stream().filter(group -> group.isSelected()).count() != 1) {
      return;
    }
    CardGroup sourceGroup = null;
    CardGroup destinationGroup = null;
    Card card = null;

    for (CardGroup group: board.groups()) {
      if (group.tag().equals(sourceTag)) {
        for (Card c: group) {
          if (c.isSelected()) {
            card = c;
            sourceGroup = group;
          }
        }
      }
    }
    for (CardGroup group: board.groups()) {
      if (group.tag().equals(destinationTag) && group != sourceGroup && group.isSelected()) {
        destinationGroup = group;
      }
    }
    if (sourceTag.equals("own")) {
      sourceGroup = board.players().stream().filter(player -> player.isActive()).findFirst().get().hand();
      for (Card c: sourceGroup) {
        if (c.isSelected()) {
          card = c;
        }
      }
    } else if (destinationTag.equals("own")) {
      CardGroup hand = board.players().stream().filter(player -> player.isActive()).findFirst().get().hand();
      if (hand.isSelected()) {
        destinationGroup = hand;
      }
    }
    if (card == null || sourceGroup == null || destinationGroup == null) {
      return;
    }
    board.selectables().forEach(selectable -> selectable.deselect());
    board.moveCard(card, destinationGroup);

  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Transfer", sourceTag, destinationTag);
  }
}
