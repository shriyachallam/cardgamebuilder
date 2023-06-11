package oogasalad.runner.model.board.backup;

import java.util.List;
import oogasalad.runner.model.card.Card;

/**
 * The CardGroupBackup record for a particular state of an arbitrary CardGroup. These can save the
 * state of player hands and groups on the board.
 *
 * @param id    id associated with each group
 * @param cards list of cards in each group
 */
public record CardGroupBackup(int id, List<Card> cards) {

}
