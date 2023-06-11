package oogasalad.runner.model.card;

/**
 * The value of a card
 */
public interface CardValue {

  /**
   * @return the number of the value in the enum
   */
  int ordinal();

  int points();

  String name();
}
