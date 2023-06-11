package oogasalad.runner.model.action.player;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
/*
given 2 cards selected, swap them
 */
public class Swap implements PlayerAction {
  private final String groupOneTag;
  private final String groupTwoTag;
  private final Board board;

  public Swap(Map<String, String> params, Board board) {
    this.groupOneTag = params.get("group_one_tag") ;
    this.groupTwoTag = params.get("group_two_tag");
    this.board = board;
  }
  @Override
  public void act() {
    if (board.cards().stream().filter(card -> card.isSelected()).count() != 2){
      return;
    }
    System.out.println("SWAP");
    CardGroup group1 = null;
    CardGroup group2 = null;
    Card card1 = null;
    Card card2 = null;
    for (CardGroup group: board.groups()) {
      if (group.tag().equals(groupOneTag) ||
      groupOneTag.equals("own") && group == board.players().stream().filter(player -> player.isActive()).findFirst().get().hand()) {
        for (Card card: group) {
          if (card.isSelected()) {
            card1 = card;
            group1 = group;
          }
        }
      }
      if (group.tag().equals(groupTwoTag) ||
          groupOneTag.equals("own") && group == board.players().stream().filter(player -> player.isActive()).findFirst().get().hand()) {
        for (Card card: group) {
          if (card.isSelected()) {
            card2 = card;
            group2 = group;
          }
        }
      }
    }
    if (card1 == null || card2 == null
    || group1 == null || group2 == null) {
      return;
    }
    board.selectables().forEach(selectable -> selectable.deselect());
    board.moveCard(card1, group2);
    board.moveCard(card2, group1);
  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Swap", groupOneTag, groupTwoTag);
  }
}
