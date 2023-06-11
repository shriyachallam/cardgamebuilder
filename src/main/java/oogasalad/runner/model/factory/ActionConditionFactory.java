package oogasalad.runner.model.factory;

import java.util.Map;
import java.util.ResourceBundle;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.backend.record.ConditionRecord;
import oogasalad.builder.backend.record.PlayerActionRecord;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.condition.Condition;
import oogasalad.runner.model.exception.MalformedRecordException;

/**
 * implements PlayerActionFactory, ComputerActionFactory, ConditionFactory.
 */
public class ActionConditionFactory implements PlayerActionFactory, ComputerActionFactory,
    ConditionFactory {

  private String File_PATH = "oogasalad.file_manager.ParserClasses";
  private ResourceBundle resources = ResourceBundle.getBundle(File_PATH);
  @Override
  public ComputerAction createComputerAction(ComputerActionRecord record, Board board)
      throws MalformedRecordException {
    try {
      return (ComputerAction) Class.forName(resources.getString(record.name()))
          .getDeclaredConstructor(Map.class, Board.class).newInstance(record.parameters(), board);
    } catch (Exception e) {
      throw new MalformedRecordException(record.name());
    }
  }
  @Override
  public Condition createCondition(ConditionRecord record, Board board) throws MalformedRecordException {
    try {
      return (Condition) Class.forName(resources.getString(record.name()))
          .getDeclaredConstructor(Map.class, Board.class).newInstance(record.parameters(), board);
    } catch (Exception e) {
      throw new MalformedRecordException(record.name());
    }
  }

  @Override
  public PlayerAction createPlayerAction(PlayerActionRecord record, Board board)
      throws MalformedRecordException {
    try {
      return (PlayerAction) Class.forName(resources.getString(record.name()))
          .getDeclaredConstructor(Map.class, Board.class).newInstance(record.parameters(), board);
    } catch (Exception e) {
      throw new MalformedRecordException(record.name());
    }
  }
}
