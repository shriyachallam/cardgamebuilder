package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
/*
checks the total amount of conditions match the input for that phase
 */
public class TransferAmountCondition implements Condition {
  private final int amount;
  private final Board board;
  public TransferAmountCondition(Map<String, String> params, Board board) {
    amount = Integer.parseInt(params.get("amount"));
    this.board = board;
  }
  @Override
  public boolean isValid() {
    return board.getPhaseTransfers() == amount;
  }
  @Override
  public TranslationPackage description() {
    return new TranslationPackage("TransferAmountCondition", Integer.toString(amount));
  }
}
