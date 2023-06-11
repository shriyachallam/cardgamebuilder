package oogasalad.runner.model.factory;

import oogasalad.builder.backend.record.ConditionRecord;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.condition.Condition;
import oogasalad.runner.model.exception.MalformedRecordException;

/**
 * A factory that creates Conditions
 */
public interface ConditionFactory {

  Condition createCondition(ConditionRecord record, Board board) throws MalformedRecordException;
}
