package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.CardSuit;
import oogasalad.runner.model.card.group.CardGroup;
/*
checks that a set is a sequence of cards all with the same suit
 */
public class RunCondition extends GroupCondition {

  public RunCondition(Map<String, String> params, Board board) {
    super(params, board);
  }

  protected boolean isValid(CardGroup group) {
    if (group.size() < 3) {
      return false;
    }
    CardSuit suit = group.cardAt(0).suit();
    for (Card card : group) {
      if (card.suit() != suit) {
        return false;
      }
    }
    group.sort();
    int starting_idx;
    if (group.cardAt(0).value().ordinal() == 0
        && group.cardAt(group.size() - 1).value().ordinal() == 12) {
      starting_idx = 1;
    } else {
      starting_idx = 0;
    }
    for (int i = starting_idx; i < group.size() - 1; i++) {
      if (group.cardAt(i).value().ordinal() + 1 != group.cardAt(i + 1).value().ordinal()) {
        return false;
      }
    }
    return true;
  }


  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Run", getTag());
  }
}
