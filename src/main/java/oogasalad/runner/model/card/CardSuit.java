package oogasalad.runner.model.card;

/**
 * The suit of a card
 */
public interface CardSuit {

  /**
   * @return the number of the suit in the enum
   */
  int ordinal();

  String name();
}
