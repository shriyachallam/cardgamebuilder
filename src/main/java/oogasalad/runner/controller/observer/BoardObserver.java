package oogasalad.runner.controller.observer;

import oogasalad.runner.model.board.PlayerBoard;

public interface BoardObserver extends Observer {
  void linkBoard(PlayerBoard board);
  Runnable getBoardCallback();
}
