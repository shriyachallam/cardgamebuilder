package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

/*
chekcs that the number of cards in a group with the input value is less than or equal to 1
*/
public class ValueCondition extends GroupCondition {

  public ValueCondition(Map<String, String> params,
      Board board) {
    super(params, board);
  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Value", getTag());
  }

  @Override
  protected boolean isValid(CardGroup pile) {
    return pile.stream().map(card -> card.value()).distinct().count() <= 1;
  }
}
