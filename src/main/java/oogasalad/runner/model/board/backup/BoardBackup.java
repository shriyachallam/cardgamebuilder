package oogasalad.runner.model.board.backup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.CardGroup.ViewType;
import oogasalad.runner.model.exception.NonexistentIDException;
import oogasalad.runner.model.player.Player;

/**
 * The BoardBackup class is responsible for saving the state of a board during a player turn. When
 * needed (for example, when a player makes an invalid move), this class can be referenced to
 * recover a previous valid board state.
 *
 * @author alecliu
 */
public class BoardBackup {

  private final List<CardGroupBackup> activeGroups;

  /**
   * Class constructor
   *
   * @param currentBoard current board state
   */
  public BoardBackup(ConcreteBoard currentBoard) {
    activeGroups = new ArrayList<>();
    copyBoard(currentBoard);
    copyPlayerHands(currentBoard);
  }

  /**
   * Copy the arrangement of cards on the board
   *
   * @param currentBoard
   */
  private void copyBoard(ConcreteBoard currentBoard) {
    Set<CardGroup> currentGroups = currentBoard.groups();
    for (CardGroup group : currentGroups) {
      ArrayList<Card> cards = new ArrayList<>();
      for (Card card : group) {
        cards.add(card);
      }
      if(group.getViewType() == ViewType.STACK){
        Collections.reverse(cards);
      }
      activeGroups.add(new CardGroupBackup(group.id(), cards));
    }
    for (Player player : currentBoard.players()) {
      activeGroups.add(new CardGroupBackup(player.hand().id(), player.hand().stream().toList()));
    }
  }

  /**
   * Copy the arrangement of all cards in player hands
   *
   * @param currentBoard current board
   */
  private void copyPlayerHands(ConcreteBoard currentBoard) {
    List<Player> players = currentBoard.players();
    for (Player player : players) {
      List<Card> cards = new ArrayList<>();
      player.hand().forEach(cards::add);
      activeGroups.add(
          new CardGroupBackup(player.hand().id(), cards));
    }
  }

  /**
   * Get the saved state of a group of cards for an associated ID
   *
   * @param id id number
   * @return list of cards previously saved
   * @throws NonexistentIDException if the id does not exist in the backup
   */
  public List<Card> getSavedCards(int id) throws NonexistentIDException {
    for (CardGroupBackup backup : activeGroups) {
      if (backup.id() == id) {
        return backup.cards();
      }
    }
    throw new NonexistentIDException();
  }
}
