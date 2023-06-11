package oogasalad.runner.model.stage;

import java.util.List;
import java.util.stream.Collectors;

import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.condition.Condition;
import oogasalad.runner.model.factory.ActionConditionFactory;
import oogasalad.runner.model.factory.ConditionFactory;
import oogasalad.runner.model.factory.PlayerActionFactory;

/**
 * A phase is a collection of actions that are executed before and after the transfers. The phase
 * also has a minimum and maximum number of transfers, and a direction. The direction is either 1 or
 * -1, and determines whether the transfers are from the player to the CPU or vice versa.
 */
public class Phase implements Stage {

  private final List<Condition> conditions;//Each condition receive a Map of paramters

  private final List<PlayerAction> playerActions;

  private final Board board;
  private String description;

  public Phase(PhaseRecord phaseRecord, Board board) {
    ActionConditionFactory factory = new ActionConditionFactory();
    PlayerActionFactory playerActionFactory = factory;
    this.playerActions = phaseRecord.playerActions().stream()
        .map(playerActionRecord -> playerActionFactory.createPlayerAction(playerActionRecord, board))
        .collect(Collectors.toList());
    ConditionFactory conditionFactory = factory;
    this.conditions = phaseRecord.conditions().stream()
        .map(conditionRecord -> conditionFactory.createCondition(conditionRecord, board))
        .collect(Collectors.toList());
    this.board = board;
    this.description = phaseRecord.description();
  }
  @Override
  public Status step() {
    board.setAllowedMoves(playerActions);
    board.setPhaseConditions(conditions);
    board.setPhaseDescription(description);
    board.saveBackup();
    return Status.DONE;
  }

  /**
   * Return description of Phase for display in UI (above list of valid actions)
   * @return
   */
  public String getDescription() {
    return description;
  }
}
