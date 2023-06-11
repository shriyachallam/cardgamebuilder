package oogasalad.runner.model.card.group;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import oogasalad.runner.model.board.Selectable;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.exception.CardNotFoundException;

/**
 * A CardGroup is a collection of cards that can be drawn from, added to, and sorted.
 */
public interface CardGroup extends ImmutableCardGroup, Selectable {
  enum ViewType {
    SEQUENCE, STACK
  }
  enum Direction {
    FACE_UP, FACE_DOWN
  }

  void add(Card card);

  void remove(Card card);

  void removeByIndex(int index);


  /**
   * Returns the top card of the card group
   *
   * @return
   */

  Card draw() throws CardNotFoundException;

  Card draw(int index) throws CardNotFoundException;

  void sort();

  void shuffle();

  int getCardScore();
  ViewType getViewType();
  void clear();
  void addAll(Collection<Card> cards);
  Stream<Card> stream();
}
