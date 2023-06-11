package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;
/*
given a card group, checks that it is empty
 */
public class EmptyCondition extends GroupCondition {

  public EmptyCondition(Map<String, String> params, Board board) {
    super(params, board);
  }

  @Override
  protected boolean isValid(CardGroup group) {
    return group.isEmpty();
  }


  @Override
  public TranslationPackage description() {
    return new TranslationPackage("Empty", getTag());
  }
}
