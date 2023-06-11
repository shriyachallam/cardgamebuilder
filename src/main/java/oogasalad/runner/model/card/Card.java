package oogasalad.runner.model.card;

import oogasalad.runner.model.board.Selectable;

/**
 * A card is a playing card that has a suit and a value
 *
 * @param <SUIT>
 * @param <VALUE>
 */

public interface Card<SUIT extends CardSuit, VALUE extends CardValue> extends ImmutableCard<SUIT, VALUE>,
    Selectable {
}
