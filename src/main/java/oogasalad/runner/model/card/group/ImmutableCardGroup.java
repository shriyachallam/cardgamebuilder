package oogasalad.runner.model.card.group;

import java.util.stream.Stream;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.ImmutableCard;
import oogasalad.runner.model.card.group.CardGroup.Direction;
import oogasalad.runner.model.card.group.CardGroup.ViewType;

public interface ImmutableCardGroup extends Iterable<Card> {

  int id();

  String tag();
  ViewType type();
  Direction direction();
  Card cardAt(int index);

  int indexOf(Card card);

  int size();

  boolean contains(Card card);
  boolean isEmpty();

  boolean isSelected();

  Stream<? extends ImmutableCard> stream();

}
