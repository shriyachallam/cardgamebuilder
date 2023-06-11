package oogasalad.runner.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.Card;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import oogasalad.runner.model.player.ConcretePlayer;
import oogasalad.runner.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for ConcreteBoard
 */
class ConcreteBoardTest {

  ConcreteBoard board;

  @BeforeEach
  void setUp() {
    board = new ConcreteBoard(() -> {});
  }

  @Test
  void testGroups() {
    CardGroup group = new ConcreteCardGroup();
    board.addGroup(group);
    assertEquals(1, board.groups().size());
    board.addGroup(new ConcreteCardGroup());
    assertTrue(board.groups().contains(group));
    assertEquals(2, board.groups().size());
  }

  @Test
  void testPlayers() {
    Player player = new ConcretePlayer("test");
    board.addPlayer(player);
    assertTrue(board.players().contains(player));
    assertEquals(1, board.players().size());
    assertEquals(1, board.groups().size());
  }

  @Test
  void testTransfer() {
    CardGroup source = new ConcreteCardGroup();
    CardGroup destination = new ConcreteCardGroup();
    Card card = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    source.add(card);
    board.addGroup(source);
    board.addGroup(destination);
    board.transfer(source, destination, 1);
    assertEquals(0, source.size());
    assertEquals(1, destination.size());
  }

  @Test
  void testMoveCard() {
    CardGroup source = new ConcreteCardGroup();
    CardGroup destination = new ConcreteCardGroup();
    Card card = new StandardCard(StandardSuit.CLUB, StandardValue.ACE);
    source.add(card);
    board.addGroup(source);
    board.addGroup(destination);
    board.moveCard(card, destination);
    assertEquals(0, source.size());
    assertEquals(1, destination.size());
  }

  @Test
  void testBackupFeature() {
    // set up initial players
    Player p1 = new ConcretePlayer("Player 1");
    Card player1Card = new StandardCard(StandardSuit.DIAMOND, StandardValue.JACK);
    p1.hand().clear();
    p1.hand().addAll(List.of(player1Card));
    board.addPlayer(p1);

    Player p2 = new ConcretePlayer("Player 2");
    Card player2Card = new StandardCard(StandardSuit.HEART, StandardValue.JACK);
    p2.hand().clear();
    p2.hand().addAll(List.of(player2Card));
    board.addPlayer(p2);

    Player p3 = new ConcretePlayer("Player 3");
    Card player3Card = new StandardCard(StandardSuit.CLUB, StandardValue.JACK);
    p3.hand().clear();
    p3.hand().addAll(List.of(player3Card));
    board.addPlayer(p3);

    Player p4 = new ConcretePlayer("Player 4");
    Card player4Card = new StandardCard(StandardSuit.SPADE, StandardValue.JACK);
    p4.hand().clear();
    p4.hand().addAll(List.of(player4Card));
    board.addPlayer(p4);

    // set up initial groups on board
    CardGroup runOnBoard = new ConcreteCardGroup();
    Card r1 = new StandardCard(StandardSuit.HEART, StandardValue.FOUR);
    Card r2 = new StandardCard(StandardSuit.HEART, StandardValue.FIVE);
    Card r3 = new StandardCard(StandardSuit.HEART, StandardValue.SIX);
    runOnBoard.clear();
    runOnBoard.addAll(List.of(r1, r2, r3));
    board.addGroup(runOnBoard);

    CardGroup meldOnBoard = new ConcreteCardGroup();
    Card m1 = new StandardCard(StandardSuit.HEART, StandardValue.FOUR);
    Card m2 = new StandardCard(StandardSuit.CLUB, StandardValue.FOUR);
    Card m3 = new StandardCard(StandardSuit.SPADE, StandardValue.FOUR);
    meldOnBoard.clear();
    meldOnBoard.addAll(List.of(m1, m2, m3));
    board.addGroup(meldOnBoard);

    // Cycle through many iterations of save-restore
    for (int i = 0; i < 100; i++) {
      // save a backup
      board.saveBackup();

      // "play this phase" aka disrupt board, make all players play their hands in different places
      p1.hand().remove(player1Card);
      p2.hand().remove(player2Card);
      p3.hand().remove(player3Card);
      p4.hand().remove(player4Card);
      runOnBoard.clear();
      runOnBoard.addAll(List.of(r1, r2, r3, player1Card, player2Card));
      meldOnBoard.clear();
      meldOnBoard.addAll(List.of(m1, m2, m3, player3Card, player4Card));

      // restore board state
      board.revertState();
    }

    assertEquals(3, runOnBoard.size());
    assertEquals(3, meldOnBoard.size());
    assertEquals(1, p1.hand().size());
    assertEquals(1, p2.hand().size());
    assertEquals(1, p3.hand().size());
    assertEquals(1, p4.hand().size());
  }

}