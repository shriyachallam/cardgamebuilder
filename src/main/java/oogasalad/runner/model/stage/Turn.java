package oogasalad.runner.model.stage;

import java.util.Iterator;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.board.Board;

/**
 * Turn class stores a list of phases in a turn.
 */
public class Turn implements Stage {
  private final ComputerAction beforeAction;
  private final ComputerAction afterAction;
  private final Iterator<Phase> phaseIterator;
  private Phase currentPhase;
  private boolean started;
  // step function
  public Turn(TurnRecord turnRecord, Board board) {
    beforeAction = Stage.makeBeforeAction(turnRecord, board);
    afterAction = Stage.makeAfterAction(turnRecord, board);
    this.phaseIterator = turnRecord.stages().stream()
        .map(phaseRecord -> new Phase(phaseRecord, board)).iterator();
    currentPhase = phaseIterator.next();
    started = false;
  }
  @Override
  public Status step() {
    if (!started) {
      beforeAction.act();
      started = true;
    }
    currentPhase.step();
    if (phaseIterator.hasNext()) {
      currentPhase = phaseIterator.next();
      return Status.ACTIVE;
    } else {
      afterAction.act();
      return Status.DONE;
    }
  }
}