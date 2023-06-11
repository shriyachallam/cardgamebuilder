package oogasalad.runner.model.card.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.ImmutableCard;
import oogasalad.runner.model.exception.CardNotFoundException;

public class ConcreteCardGroup implements CardGroup {

  private final String tag;
  private final ViewType viewType;
  private final Direction direction;
  private boolean selected = false;
  private int id;

  private final List<Card> cards = new ArrayList<>();

  public ConcreteCardGroup(String tag, ViewType viewType, Direction direction) {
    this.tag = tag;
    this.viewType = viewType;
    this.direction = direction;
    this.id = this.hashCode();
  }

  public ConcreteCardGroup() {
    this.tag = "group";
    this.viewType = ViewType.SEQUENCE;
    this.direction = Direction.FACE_UP;
    this.id = this.hashCode();
  }

  public String tag() {
    return tag;
  }

  @Override
  public ViewType type() {
    return viewType;
  }

  @Override
  public Direction direction() {
    return direction;
  }
  @Override
  public void add(Card card) {
    cards.add(0, card);
  }
  @Override
  public void remove(Card card) {
    cards.remove(card);
  }

  @Override
  public void removeByIndex(int index){
    cards.remove(index);
  }

  @Override
  public Card draw() throws CardNotFoundException {
    try {
      Card drawnCard = cards.get(0);
      cards.remove(0);
      return drawnCard;
    } catch (IndexOutOfBoundsException e) {
      throw new CardNotFoundException();
    }
  }

  @Override
  public Card draw(int index) throws CardNotFoundException {
    try {
      Card drawnCard = cards.get(index);
      cards.remove(index);
      return drawnCard;
    } catch (IndexOutOfBoundsException e) {
      throw new CardNotFoundException();
    }
  }

  public Card cardAt(int index) {
    return cards.get(index);
  }

  @Override
  public int indexOf(Card card) {
    return cards.indexOf(card);
  }

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public boolean isSelected() {
    return selected;
  }

  @Override
  public Stream<Card> stream() {
    return cards.stream();
  }

  @Override
  public int id() {
    return id;
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public boolean contains(Card card) {
    return cards.contains(card);
  }

  @Override
  public Iterator<Card> iterator() {
    List<Card> copy = new ArrayList<>(cards);
    Collections.reverse(copy);
    return copy.iterator();
  }


  @Override
  public void sort() {
    Collections.sort(cards);
  }

  @Override
  public void shuffle() {
    Collections.shuffle(cards);
  }


  @Override
  public int getCardScore() {
    int score = 0;
    for (Card card : cards) {
      score += card.value().points();
    }
    return score;
  }

  @Override
  public ViewType getViewType() {
    return viewType;
  }

  @Override
  public void clear() {
    cards.clear();
  }

  @Override
  public void addAll(Collection<Card> cards) {
    this.cards.addAll(cards);
  }
  @Override
  public void select() {
    this.selected = true;
  }
  @Override
  public void deselect() {
    this.selected = false;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof CardGroup other) {
      return this.id() == other.id();
    }
    return false;
  }
}

