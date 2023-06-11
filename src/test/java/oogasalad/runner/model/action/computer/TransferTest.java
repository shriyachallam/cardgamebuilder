package oogasalad.runner.model.action.computer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.StandardCard;
import oogasalad.runner.model.card.StandardCard.StandardSuit;
import oogasalad.runner.model.card.StandardCard.StandardValue;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

class TransferTest {
  @Test
  void test() {
    CardGroup source = new ConcreteCardGroup("source", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    CardGroup destination = new ConcreteCardGroup("destination", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    Board board = new ConcreteBoard(() -> {});
    board.addGroup(source);
    board.addGroup(destination);
    Transfer action = new Transfer(Map.of("source_tag", "source", "destination_tag", "destination", "amount", "2"), board);
    source.add(new StandardCard(StandardSuit.CLUB, StandardValue.ACE));
    source.add(new StandardCard(StandardSuit.CLUB, StandardValue.TWO));
    source.add(new StandardCard(StandardSuit.CLUB, StandardValue.THREE));
    assertEquals(3, source.size());
    assertEquals(0, destination.size());
    action.act();
    assertEquals(1, source.size());
    assertEquals(2, destination.size());

  }

}