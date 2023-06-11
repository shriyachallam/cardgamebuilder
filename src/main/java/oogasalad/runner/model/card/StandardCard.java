package oogasalad.runner.model.card;

import java.util.HashSet;
import java.util.Set;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;

/**
 * A standard playing card.
 *
 */
public class StandardCard implements Card<StandardSuit, StandardValue> {

  private final StandardSuit suit;
  private final StandardValue value;
  private boolean selected;

  public StandardCard(StandardSuit suit, StandardValue value) {
    this.suit = suit;
    this.value = value;
  }

  public static Set<StandardCard> all() {
    Set<StandardCard> cards = new HashSet<>();
    for (StandardSuit suit : StandardSuit.values()) {
      for (StandardValue value : StandardValue.values()) {
        cards.add(new StandardCard(suit, value));
      }
    }
    return cards;
  }

  @Override
  public StandardSuit suit() {
    return suit;
  }

  @Override
  public StandardValue value() {
    return value;
  }

  @Override
  public int id() {
    return this.hashCode();
  }

  @Override
  public boolean isSelected() {
    return selected;
  }

  @Override
  public void select() {
    selected = true;
  }

  @Override
  public void deselect() {
    selected = false;
  }


  public enum StandardSuit implements CardSuit {
    CLUB, DIAMOND, HEART, SPADE;
  }

  public enum StandardValue implements CardValue {
    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

    @Override
    public int points() {
      if (this.ordinal() == 0 || this.ordinal() > 9) {
        return 10;
      } else {
        return this.ordinal() + 1;
      }
    }
  }

  @Override
  public String toString() {
    return this.value.toString() + " of " + this.suit.toString();
  }

}
