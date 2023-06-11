package oogasalad.runner.model.action.computer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.card.group.ConcreteCardGroup;
import org.junit.jupiter.api.Test;

class PlaceDeckTest {
  @Test
  void test() {
    Board board = new ConcreteBoard(() -> {});
    PlaceDeck action = new PlaceDeck(Map.of("group_tag", "test"), board);
    CardGroup group = new ConcreteCardGroup("test", CardGroup.ViewType.SEQUENCE, CardGroup.Direction.FACE_UP);
    board.addGroup(group);
    assertEquals(0, group.size());
    action.act();
    assertEquals(52, group.size());
    action.act();
    assertEquals(104, group.size());
  }

}