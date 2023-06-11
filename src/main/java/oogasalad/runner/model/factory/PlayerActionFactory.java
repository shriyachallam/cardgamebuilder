package oogasalad.runner.model.factory;

import oogasalad.builder.backend.record.PlayerActionRecord;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.exception.MalformedRecordException;

/**
 * A factory that creates PlayerActions.
 */
public interface PlayerActionFactory {

  PlayerAction createPlayerAction(PlayerActionRecord record, Board board) throws MalformedRecordException;

}
