package oogasalad.runner.model.factory;

import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.exception.MalformedRecordException;

/**
 * A factory that creates ComputerActions
 */
public interface ComputerActionFactory {

  ComputerAction createComputerAction(ComputerActionRecord record, Board board) throws MalformedRecordException;

}
