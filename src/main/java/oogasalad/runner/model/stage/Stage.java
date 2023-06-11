package oogasalad.runner.model.stage;

import oogasalad.builder.backend.record.StageRecord;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.action.computer.ComputerActionList;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.factory.ActionConditionFactory;
import oogasalad.runner.model.factory.ComputerActionFactory;

/**
 * A Stage is a step in the game. It has a step() method that updates the status of the game based
 * on internal state and the state of the board. It is implemented by the Game, Round,
 * Turn, and Phase classes
 *
 * @author Delali Cudjoe
 */
public interface Stage {
  static ComputerAction makeBeforeAction(StageRecord<?> record, Board board) {
    ComputerActionFactory factory = new ActionConditionFactory();
    return new ComputerActionList(record.beforeActions().stream()
        .map(computerActionRecord -> factory.createComputerAction(computerActionRecord, board))
        .toArray(ComputerAction[]::new));
  }
  static ComputerAction makeAfterAction(StageRecord<?> record, Board board) {
    ComputerActionFactory factory = new ActionConditionFactory();
    return new ComputerActionList(record.afterActions().stream()
        .map(computerActionRecord -> factory.createComputerAction(computerActionRecord, board))
        .toArray(ComputerAction[]::new));
  }

  Status step(); // after a step, one of the two statuses (statii??) are returned

  enum Status {DONE, ACTIVE}
}
