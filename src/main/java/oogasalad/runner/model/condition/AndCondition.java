package oogasalad.runner.model.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;

/**
 * class used for combining conditions. similar to a list of conditions, but knows internally to
 * return true iff all included conditions also return true;
 */
public class AndCondition implements Condition {

  private final List<Condition> conditions;
  private final Board board;

  public AndCondition(Board board, Condition... conditions) {
    this.conditions = new ArrayList<>(Arrays.asList(conditions));
    this.board = board;
  }

  @Override
  public boolean isValid() {
    for (Condition condition : conditions) {
      if (!condition.isValid()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public TranslationPackage description() {
    return null;
  }

  public void add(Condition c) {
    conditions.add(c);
  }

}
