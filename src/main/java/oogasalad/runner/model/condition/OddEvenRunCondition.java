package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;
/*
either odd or even condition satisfied
 */
public class OddEvenRunCondition extends GroupCondition {

  private final OddRunCondition oddRunCondition;
  private final EvenRunCondition evenRunCondition;
  private String tag;

  public OddEvenRunCondition(Map<String, String> params, Board board) {
    super(params, board);
    oddRunCondition = new OddRunCondition(params, board);
    evenRunCondition = new EvenRunCondition(params, board);
  }

  @Override
  protected boolean isValid(CardGroup pile) {
    return oddRunCondition.isValid() || evenRunCondition.isValid();
  }

  @Override
  public TranslationPackage description() {
    return null;
    //return new TranslationPackage("OddEvenRunCondition", oddRunCondition.getTag());
  }
}
