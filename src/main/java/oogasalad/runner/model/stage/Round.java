package oogasalad.runner.model.stage;

import java.util.Iterator;
import java.util.List;
import oogasalad.builder.backend.record.RoundRecord;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.player.Player;
public class Round implements Stage {
  private final ComputerAction beforeAction;
  private final ComputerAction afterAction;
  private final Board board;
  private boolean started;
  private Turn currentTurn;
  private Iterator<Turn> turnIterator;
  private Iterator<Player> playerIterator;
  private List<TurnRecord> turnRecords;
  private Player currentPlayer;
  public Round(RoundRecord roundRecord, Board board) {
    beforeAction = Stage.makeBeforeAction(roundRecord, board);
    afterAction = Stage.makeAfterAction(roundRecord, board);
    turnRecords = roundRecord.stages();
    turnIterator = turnRecords.stream()
        .map(turnRecord -> new Turn(turnRecord, board)).iterator();
    currentTurn = turnIterator.next();
    this.board = board;
  }
  @Override
  public Status step() {
    if (!started) {
      playerIterator = board.players().iterator();
      currentPlayer = playerIterator.next();
      beforeAction.act();
      started = true;
    }
    board.players().forEach(player -> player.setActive(false));
    currentPlayer.setActive(true);
    Status status = currentTurn.step();
    if (status == Status.DONE) {
      if (turnIterator.hasNext()) {
        currentTurn = turnIterator.next();
        afterAction.act();
        return Status.ACTIVE;
      }
      else if (playerIterator.hasNext()) {
        currentPlayer = playerIterator.next();
        turnIterator = turnRecords.stream()
            .map(turnRecord -> new Turn(turnRecord, board)).iterator();
        currentTurn = turnIterator.next();
        return Status.ACTIVE;
      } else {
        afterAction.act();
        return Status.DONE;
      }

    }
    return Status.ACTIVE;
  }
}