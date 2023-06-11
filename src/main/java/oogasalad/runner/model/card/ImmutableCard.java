package oogasalad.runner.model.card;

public interface ImmutableCard<SUIT extends CardSuit, VALUE extends CardValue> extends Comparable<Card> {

  SUIT suit();

  VALUE value();

  int id();
  boolean isSelected();
  default int compareTo(Card o) {
    if (this.suit().ordinal() == o.suit().ordinal()) {
      return this.value().ordinal() - o.value().ordinal();
    }
    return this.suit().ordinal() - o.suit().ordinal();
  }

  String toString();


  default boolean equals(Card other) {
    return this.suit().equals(other.suit()) && this.value().equals(other.value());
  }
}
