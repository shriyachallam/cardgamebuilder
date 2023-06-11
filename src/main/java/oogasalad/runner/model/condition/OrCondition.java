package oogasalad.runner.model.condition;

import java.util.List;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
/*
condition that needs atleast 1 condition to be satisfied
 */
public class OrCondition implements Condition {
  private final List<Condition> conditions;
  private final Board board;
  public OrCondition(Board board, Condition... conditions) {
    this.conditions = List.of(conditions);
    this.board = board;
  }
  @Override
  public boolean isValid() {
    for (Condition condition : conditions) {
      if (condition.isValid()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public TranslationPackage description() {
    return null;
  }
}
