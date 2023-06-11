package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

/*
checks that a set has not more than 1 card of a specific suit
 */
public class SuitCondition extends GroupCondition{

  public SuitCondition(Map<String, String> params,
      Board board) {
    super(params, board);
  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Suit", getTag());
  }

  @Override
  protected boolean isValid(CardGroup group) {
    return group.stream().map(card -> card.suit()).distinct().count() <= 1;
  }
}
