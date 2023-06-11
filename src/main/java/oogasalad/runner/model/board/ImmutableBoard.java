package oogasalad.runner.model.board;

import java.util.List;
import java.util.Set;
import oogasalad.runner.controller.observer.BoardObserver;
import oogasalad.runner.controller.observer.Observable;
import oogasalad.runner.model.card.ImmutableCard;
import oogasalad.runner.model.card.group.ImmutableCardGroup;
import oogasalad.runner.model.player.ImmutablePlayer;

/**
 * Defines a game board for a card game. Cannot be modified.
 */
public interface ImmutableBoard extends Observable<BoardObserver> {
  Set<? extends ImmutableCardGroup> groups();

  List<? extends ImmutablePlayer> players();
  Set<? extends ImmutableCard> cards();

}
