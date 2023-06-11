package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

/**
 * Adding this class to the conditions of a Phase
 * will flip the "checkAllConditions" flag to false.
 * This means that the phase will be valid if any of the conditions are valid.
 */
public class OrFlagCondition implements Condition {

  public OrFlagCondition(Map<String, String> params, Board board) {
  }


  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("OrFlag");
  }
}
